#Number of bytes to Directly index all possible hashes under current hash scheme (Hash v1)
B = [3,3,3,3,2]
sum = 0
for val in B:
	sum = (sum << 2) + val
	
print(sum);