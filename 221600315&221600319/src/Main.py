# coding=utf-8
import re
import requests
respose=requests.get('http://openaccess.thecvf.com/CVPR2018.py')
text=respose.text
# print(respose.status_code)# 响应的状态码
# print(respose.content)  #返回字节信息
# print(respose.text)  #返回文本内容
urls=re.findall(r'class="ptitle".*?href="(.*?)"',text,re.S)  #re.S 把文本信息转换成1行匹配
output=open("result.txt","w",encoding='utf-8')
j=0
print(respose.encoding)
for i in urls:
    url='http://openaccess.thecvf.com/'+i
    respose = requests.get(url)
    text = respose.text
    paper_title = re.findall('id="papertitle">\n(.*?)<',text, re.S)  # re.S 把文本信息转换成1行匹配
    abstract = re.findall('id="abstract" >\n(.*?)<', text, re.S)  # re.S 把文本信息转换成1行匹配
    output.write(str(j)+"\n")
    output.write("Title: ")
    output.write(paper_title[0])
    output.write("\n")
    output.write("Abstract: ")
    output.write(abstract[0])
    output.write('\n\n\n')
    j+=1
