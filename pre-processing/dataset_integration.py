#!/bin/env python3
import sys, arff, csv

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: {} file... output".format(sys.argv[0]))
        sys.exit(1)

    input_files = sys.argv[1:-1]
    output_file = sys.argv[-1]
    csv.field_size_limit(sys.maxsize)
    data = []

    for file in input_files:
        print("{} processed".format(file))
        input_data = list(arff.load(file))
        for i, row in enumerate(input_data):
            input_data[i] = list(row)
            input_data[i][3] = row[3] == "True"
            input_data[i][5] = row[5] == "True"
        data += input_data

    print("Writing to output...")
    # There doesn't seem to be a way, with the arff module, to extract the names from the datasets
    # so they are given explicitly
    arff.dump(output_file, data, relation="mails", names=["from", "to", "subject", "html", "body", "spam"])
    print("Completed")

