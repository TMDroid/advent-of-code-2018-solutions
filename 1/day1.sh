#!/bin/bash

total=6083

for i in `seq 11 100`; do
	echo $i
	while read line; do
		sign=${line:0:1}
		magnitude=${line:1}

		save=$total
		total=`expr $total $sign $magnitude`
		#echo "$save $sign $magnitude = $total" >> already_used.txt
		echo "$total" >> already_used.txt


		duplicates=`cat already_used.txt | sort | uniq -d`
		count=`echo -n $duplicates | wc -l`
		if [[ $count -gt 0 ]]; then
			echo "Count = $count"
			echo $duplicates
			exit 1

		fi

		count=`echo $duplicates | wc -m`
	done < $1
done

echo $total
