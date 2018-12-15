from functools import reduce

serialNumber = 9810

matrix = []
matrix.append([])


def calculate_power_level_3x3(x, y):
    global matrix

    sum = 0
    for j in range(y, y + 3):
        for i in range(x, x + 3):
            sum += matrix[j][i]

    return sum


for y in range(1, 301):
    row = []
    row.append([])
    pline = ""

    for x in range(1, 301):
        rackID = x + 10
        powerLevel = (int((rackID * y + serialNumber) * rackID / 100) % 10) - 5

        row.append(powerLevel)

        pline += " " + str(powerLevel)

    print(pline + "\n")
    matrix.append(row)


_3x3_total_levels = []

for row in range(1, len(matrix) - 2):
    for column in range(1, len(matrix[row]) - 2):
        _3x3_total_levels.append([row, column, calculate_power_level_3x3(row, column)])

best = reduce(lambda a, b: a if a[2] > b[2] else b, _3x3_total_levels)

print(best)
