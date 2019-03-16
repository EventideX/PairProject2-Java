import requests
from bs4 import BeautifulSoup 

i=0
txt = open('result1.txt','w',encoding='utf-8')  
cvprurl='http://openaccess.thecvf.com/CVPR2018.py'
response=requests.get(cvprurl)
response.encoding='utf-8'
soup=BeautifulSoup(response.text,'html.parser')

for links in soup.select('.ptitle'):    #返回每个超链接
    newlinks = 'http://openaccess.thecvf.com/'+ links.select('a')[0]['href']  
    print(i,file=txt)
    response2 = requests.get(newlinks)  #打开链接
    response2.encoding = 'utf-8'
    soup2 = BeautifulSoup(response2.text,'html.parser')   #把网页内容存进容器
    Title = soup2.select('#papertitle')[0].text.strip()  #找到标题元素爬取
    print("Title:",Title,file=txt)
    Abstract = soup2.select('#abstract')[0].text.strip() #找到摘要元素爬取
    print("Abstract:",Abstract,"\n\n",file=txt)
    i=i+1