#!/usr/bin/env python3
"""
Generates src/main/resources/data.sql for seat-service from the theatre's
official seat arrangement / pricing zones.

Zones carry matinee/evening price uplift percentages. Every seat is assigned
to exactly one zone via its zone_id FK. Percentages and seat/row ranges are
taken directly from the provided seat-arrangement tables.

Assumptions (documented, since the source tables don't give explicit row widths):
  * Stalls rows each span seats 1..30.
  * Circle:  Row A 1..76, Row B 1..82, Row C 1..89, Row D 1..55, Row E 1..55.
  * Upper Circle: Row A 1..88, Row B 1..93, Row C 1..76.
    Any upper-circle seat not in UC-1/UC-2/UC-3 falls into the UC base zone.
Run:  python3 generate_seed.py > src/main/resources/data.sql
"""

# ---------------------------------------------------------------------------
# Zone definitions: fixed UUID, section, name, matinee_pct, evening_pct
# ---------------------------------------------------------------------------
ZONES = {
    # Stalls (priced by row label)
    "STALLS_PREMIUM":  ("a0000001-0000-0000-0000-000000000001", "STALLS",       "Stalls Premium (AA-DD)", 200.00, 250.00),
    "STALLS_STANDARD": ("a0000002-0000-0000-0000-000000000002", "STALLS",       "Stalls Standard (A-M)",  150.00, 175.00),
    "STALLS_REAR":     ("a0000003-0000-0000-0000-000000000003", "STALLS",       "Stalls Rear (P-V)",      100.00, 150.00),
    # Circle
    "CIRCLE_SIDE":     ("a0000004-0000-0000-0000-000000000004", "CIRCLE",       "Circle Side",            150.00, 175.00),
    "CIRCLE_MID":      ("a0000005-0000-0000-0000-000000000005", "CIRCLE",       "Circle Mid",             125.00, 150.00),
    "CIRCLE_CENTRE":   ("a0000006-0000-0000-0000-000000000006", "CIRCLE",       "Circle Centre Premium",  210.00, 220.00),
    # Upper Circle
    "UC_SIDE":         ("a0000007-0000-0000-0000-000000000007", "UPPER_CIRCLE", "Upper Circle Side",       80.00, 100.00),
    "UC_MID":          ("a0000008-0000-0000-0000-000000000008", "UPPER_CIRCLE", "Upper Circle Mid",        50.00,  70.00),
    "UC_CENTRE":       ("a0000009-0000-0000-0000-000000000009", "UPPER_CIRCLE", "Upper Circle Centre",     75.00, 100.00),
    "UC_BASE":         ("a0000010-0000-0000-0000-000000000010", "UPPER_CIRCLE", "Upper Circle Base",        0.00,   0.00),
}


def in_ranges(n, ranges):
    return any(lo <= n <= hi for lo, hi in ranges)


# ---------------------------------------------------------------------------
# STALLS: row label -> zone
# ---------------------------------------------------------------------------
STALLS_PREMIUM_ROWS = ["AA", "BB", "CC", "DD"]
STALLS_STANDARD_ROWS = [chr(c) for c in range(ord("A"), ord("M") + 1)]           # A..M
STALLS_REAR_ROWS = [chr(c) for c in range(ord("P"), ord("V") + 1)]              # P..V
STALLS_SEATS_PER_ROW = 30


def stalls_zone(row):
    if row in STALLS_PREMIUM_ROWS:
        return "STALLS_PREMIUM"
    if row in STALLS_STANDARD_ROWS:
        return "STALLS_STANDARD"
    if row in STALLS_REAR_ROWS:
        return "STALLS_REAR"
    return None


# ---------------------------------------------------------------------------
# CIRCLE: (row, seat) -> zone
# ---------------------------------------------------------------------------
CIRCLE_ROW_WIDTH = {"A": 76, "B": 82, "C": 89, "D": 55, "E": 55}

CIRCLE_SIDE = {
    "A": [(1, 6), (71, 76)],
    "B": [(1, 8), (75, 82)],
    "C": [(1, 8), (82, 89)],
}
CIRCLE_MID = {
    "A": [(7, 27), (50, 70)],
    "B": [(9, 31), (52, 74)],
    "C": [(9, 34), (56, 81)],
}
CIRCLE_CENTRE = {
    "A": [(28, 49)],
    "B": [(32, 51)],
    "C": [(35, 55)],
    "D": [(1, 55)],   # entire row
    "E": [(1, 55)],   # entire row
}


def circle_zone(row, seat):
    if row in CIRCLE_SIDE and in_ranges(seat, CIRCLE_SIDE[row]):
        return "CIRCLE_SIDE"
    if row in CIRCLE_MID and in_ranges(seat, CIRCLE_MID[row]):
        return "CIRCLE_MID"
    if row in CIRCLE_CENTRE and in_ranges(seat, CIRCLE_CENTRE[row]):
        return "CIRCLE_CENTRE"
    return None


# ---------------------------------------------------------------------------
# UPPER CIRCLE: (row, seat) -> zone; fallback UC_BASE
# ---------------------------------------------------------------------------
UC_ROW_WIDTH = {"A": 88, "B": 93, "C": 76}

UC_SIDE = {
    "A": [(1, 6), (83, 88)],
    "B": [(1, 8), (86, 93)],
    "C": [(1, 8), (69, 76)],
}
UC_MID = {
    "A": [(7, 32), (57, 82)],
    "B": [(9, 34), (80, 85)],
    "C": [(9, 24), (53, 68)],
}
UC_CENTRE = {
    "A": [(33, 56)],
    "B": [(35, 59)],
}


def uc_zone(row, seat):
    if row in UC_SIDE and in_ranges(seat, UC_SIDE[row]):
        return "UC_SIDE"
    if row in UC_MID and in_ranges(seat, UC_MID[row]):
        return "UC_MID"
    if row in UC_CENTRE and in_ranges(seat, UC_CENTRE[row]):
        return "UC_CENTRE"
    return "UC_BASE"  # all other upper circle


# ---------------------------------------------------------------------------
# Build the seat list: (section, row, seat_number, zone_key)
# ---------------------------------------------------------------------------
def build_seats():
    seats = []

    # Stalls
    for row in STALLS_PREMIUM_ROWS + STALLS_STANDARD_ROWS + STALLS_REAR_ROWS:
        z = stalls_zone(row)
        for n in range(1, STALLS_SEATS_PER_ROW + 1):
            seats.append(("STALLS", row, n, z))

    # Circle
    for row, width in CIRCLE_ROW_WIDTH.items():
        for n in range(1, width + 1):
            z = circle_zone(row, n)
            if z is None:
                raise ValueError(f"Circle seat {row}{n} matched no zone")
            seats.append(("CIRCLE", row, n, z))

    # Upper Circle
    for row, width in UC_ROW_WIDTH.items():
        for n in range(1, width + 1):
            seats.append(("UPPER_CIRCLE", row, n, uc_zone(row, n)))

    return seats


def uuid_for_seat(idx):
    # Deterministic seat UUID: b<...>-<12-digit-index>
    return f"b0000000-0000-0000-0000-{idx:012d}"


def uuid_for_perf_seat(idx):
    # Deterministic performance_seat UUID so inserts are idempotent on Postgres.
    return f"d0000000-0000-0000-0000-{idx:012d}"


PERFORMANCE_ID = "c0000000-0000-0000-0000-0000000000f1"


def sql_escape(s):
    return s.replace("'", "''")


def main():
    out = []
    out.append("-- ============================================================")
    out.append("-- AUTO-GENERATED by generate_seed.py from the official seat")
    out.append("-- arrangement / pricing zones. Do not edit by hand; edit the")
    out.append("-- generator and re-run instead.")
    out.append("-- ============================================================")
    out.append("")
    out.append("-- ---------- Seat Zones ----------")
    for key, (zid, section, name, mat, eve) in ZONES.items():
        out.append(
            "INSERT INTO seat_zone (zone_id, section, zone_name, matinee_pct, evening_pct, added_by, added_date) "
            f"VALUES ('{zid}', '{section}', '{sql_escape(name)}', {mat:.2f}, {eve:.2f}, 'admin', CURRENT_TIMESTAMP) "
            "ON CONFLICT (zone_id) DO NOTHING;"
        )
    out.append("")

    seats = build_seats()

    out.append(f"-- ---------- Seats ({len(seats)} total) ----------")
    for idx, (section, row, num, zkey) in enumerate(seats, start=1):
        seat_id = uuid_for_seat(idx)
        zone_id = ZONES[zkey][0]
        # Mark a couple of wheelchair spaces: seat 1 of each stalls rear row.
        wheelchair = "TRUE" if (section == "STALLS" and row in STALLS_REAR_ROWS and num == 1) else "FALSE"
        out.append(
            "INSERT INTO seat (seat_id, zone_id, section, row_label, seat_number, is_wheelchair_space, added_by, added_date) "
            f"VALUES ('{seat_id}', '{zone_id}', '{section}', '{row}', {num}, {wheelchair}, 'admin', CURRENT_TIMESTAMP) "
            "ON CONFLICT (seat_id) DO NOTHING;"
        )
    out.append("")

    out.append("-- ---------- Performance seats (soft ref to catalogue performance) ----------")
    out.append(f"-- performance_id {PERFORMANCE_ID} -> demo performance")
    for idx, (section, row, num, zkey) in enumerate(seats, start=1):
        seat_id = uuid_for_seat(idx)
        perf_seat_id = uuid_for_perf_seat(idx)
        # Seed most as AVAILABLE; make a small deterministic mix of BOOKED/HELD.
        if idx % 25 == 0:
            status, sess, held = "BOOKED", "NULL", "NULL"
        elif idx % 25 == 7:
            status = "HELD"
            sess = "'session-token-" + f"{idx:04d}" + "'"
            held = "CURRENT_TIMESTAMP + INTERVAL '15 minutes'"
        else:
            status, sess, held = "AVAILABLE", "NULL", "NULL"
        out.append(
            "INSERT INTO performance_seat (perf_seat_id, performance_id, seat_id, status, held_by_session, held_until, added_by, added_date) "
            f"VALUES ('{perf_seat_id}', '{PERFORMANCE_ID}', '{seat_id}', '{status}', {sess}, {held}, 'admin', CURRENT_TIMESTAMP) "
            "ON CONFLICT (performance_id, seat_id) DO NOTHING;"
        )
    out.append("")

    print("\n".join(out))


if __name__ == "__main__":
    main()
