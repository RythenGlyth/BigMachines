#!/bin/bash

metals=( aluminium copper lead silver tin )
# destination folder
destF=./dest/

for base in ./*metal*.json; do
	for metal in "${metals[@]}"; do
		dest=$destF$(echo $base | sed -e "s/metal/$metal/g")
		echo $dest
		cat $base | sed -e "s/metal/$metal/g" | sed -e "s/Metal/${metal^}/g" > $dest
	done
done
