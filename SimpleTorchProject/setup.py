#downloads file

#i'll get around to fully creating this file for downloads, or push them to git
#right now am focusing on data cleaning + getting data prepared for training.

import requests

url = "https://ftp.ncbi.nlm.nih.gov/genomes/all/GCF/000/001/405/GCF_000001405.26_GRCh38/GCF_000001405.26_GRCh38_feature_table.txt.gz"

print("Downloading feature table...")
response = requests.get(url)

with open("data/feature_table.txt.gz", 'wb') as f:
    f.write(response.content)

print("Downloaded!")
#wget https://ftp.ncbi.nlm.nih.gov/genomes/all/GCF/000/001/405/GCF_000001405.26_GRCh38/GCF_000001405.26_GRCh38_feature_table.txt.gz

# Or using curl
#curl -O https://ftp.ncbi.nlm.nih.gov/genomes/all/GCF/000/001/405/GCF_000001405.26_GRCh38/GCF_000001405.26_GRCh38_feature_table.txt.gz