#!/bin/bash
awk -F'\t' '{if($9 >= 0.95) {print $2";"$3";"$4";"$9}}' $1 > z.csv

sed '/,,/d' z.csv > Reverb.csv
sed '/\$/d' Reverb.csv > y.csv
sed '/\%/d' y.csv > x.csv
sed '/\:/d' x.csv > y.csv
sed '/[0-9]-[0-9]/d' y.csv > ReverbFilter.csv

rm z.csv
rm y.csv
rm x.csv


