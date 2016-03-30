#!/usr/bin/python

import sys, getopt, urllib.request, json, math

def usage():
    print("budget is a method for interacting with Jamie's Budget App over the command line")
    print("Options")
    print("-h help")
    print("-l list all budget entries")
    print("-a or --add add an entry")
    print("-u or --update updates an entry specified by passed id")
    print("-d or --delete deletes an entry specified by passed id")
    print("-t or --table specifies which table should be used (expenses, incomes, monthlyexpenses, monthlyincomes, planexpenses, planincomes) if not specified expenses is assumed")
    print("-s or --sort lists and sorts the output by the passed column (amount, date, category) use l or s")

def getID(table):
    return {
        'expenses': 'expense_id',
        'incomes': 'income_id',
        'planexpenses': 'expense_id',
        'planincomes': 'income_id',
        'monthlyexpenses': 'monthly_expense_id',
        'monthlyincomes': 'monthly_income_id'
    }[table]

def getTabs(string):
    nums=math.floor((len(string)-1)/7)
    ret=""
    for i in range(0, 3-nums):
        ret+="\t"
    return ret
    

def lister(sortby, table):
    idname=getID(table)
    sortquery=""
    if(sortby!=""):
        sortquery="?sortby="+sortby
    url="http://localhost:3420/api/"+table+sortquery
    print(url)
    response=urllib.request.urlopen(url)
    jsonres=json.loads(response.read().decode(response.info().get_param('charset') or 'utf-8'))
    datall=jsonres['data']
    if(table[0]=='m'):
        print("ID\tAmount\tCategory\t\tDescription")
        for data in datall:
            print(str(data[idname])+"\t$"+str(data['amount'])+"\t"+data['category']+getTabs(data['category'])+data['description'])
    elif(table[0]=='p'):
        print("ID\tAmount\tDate\tCategory")
        for data in datall:
            print(str(data[idname])+"\t$"+str(data['amount'])+"\t"+str(data['date'])[0:10]+"\t"+data['category'])
    else:
        print("ID\tAmount\tDate\tCategory\t\tDescription")
        for data in datall:
            print(str(data[idname])+"\t$"+str(data['amount'])+"\t"+str(data['date'])[0:10]+"\t"+data['category']+getTabs(data['category'])+data['description'])
    
try:
    opts, args = getopt.getopt(sys.argv[1:], 'hlu:ad:s:t:', ["list", "update=", "add", "delete=", "sort=", "table="])
except getopt.GetoptError:
    usage()
    sys.exit(2)

listem=False
addem=False
updatem=False
updateid=0
deletem=False
deleteid=-1
sort=""
table="expenses"
for opt, arg in opts:
    if(opt in ('-h')):
        usage()
        sys.exit(2)
    elif opt in ('-a', '--add'):
        addem=True
    elif opt in ('-u', '--update'):
        updatem=True
        updateid=arg
    elif opt in ('-d', '--delete'):
        deletem=True
        deleteid=arg
    elif opt in ('-l'):
        listem=True
    elif opt in ('-s', '--sort'):
        if arg in ('date', 'amount', 'category'):
            sort=arg
        else:
            print("Sort must be one of the following: date, amount, category")
    elif opt in ('-t', '--table'):
        if arg in ('expenses', 'incomes', 'monthlyexpenses', 'monthlyincomes', 'planexpenses', 'planincomes'):
            table=arg
        else:
            print("Table must be one of the following: expenses, incomes, monthlyexpenses, monthlyincomes, planexpenses, planincomes")
    else:
        usage()
        sys.exit(2)

if listem:
    lister(sort, table)
if addem:
    add(table)
if updatem:
    update(table, updateid)
if deletem:
    delete(table, deleteid)
