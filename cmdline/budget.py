#!/usr/bin/python

import sys, getopt, urllib.request, json, math, serverloc, datetime

#Define all allowed categories
categories=["Rent/Mortgage", "Home Insurance", "Home Maintenance", "Food", "Electric/Gas/Water", "Internet/Phone", "Car Payment", "Cat Insurance", "Gas/Parking", "Car Maintenance", "Next Car", "Outings", "Vacation Fund", "Electronics", "Games", "Misc. Entertainment", "Snacks", "Health Insurance", "Life Insurance", "Student Loans", "Credit Card Loans", "Other Loans", "Charity", "Savings", "Emergency Fund"]

#print out the proper usage
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

#return the id name of the given table
def getID(table):
    return {
        'expenses': 'expense_id',
        'incomes': 'income_id',
        'planexpenses': 'expense_id',
        'planincomes': 'income_id',
        'monthlyexpenses': 'monthly_expense_id',
        'monthlyincomes': 'monthly_income_id'
    }[table]

#Get the proper number of tabs to align the categories column
def getTabs(string):
    nums=math.floor((len(string)-1)/7)
    ret=""
    for i in range(0, 3-nums):
        ret+="\t"
    return ret
    
#list elements in the given table sorted by the given sort element, default to expenses table and id for sorting
def lister(sortby, table):
    idname=getID(table)
    #define an empty sort query which will be replaced if a sort type was given
    sortquery=""
    if(sortby!=""):
        sortquery="?sortby="+sortby
    #Define the url to call given a server location in serverloc, a table name, and a sort query
    url="http://"+serverloc.serverloc+"/api/"+table+sortquery
    response=urllib.request.urlopen(url)
    #Convert the call to json and then get the data element from the json data
    jsonres=json.loads(response.read().decode(response.info().get_param('charset') or 'utf-8'))
    datall=jsonres['data']
    #if the table is a monthly expense table print the data ignoring date
    if(table[0]=='m'):
        print("ID\tAmount\tCategory\t\tDescription")
        for data in datall:
            print(str(data[idname])+"\t$"+str(data['amount'])+"\t"+data['category']+getTabs(data['category'])+data['description'])
    #if the table is a planning table print the data ignorining description
    elif(table[0]=='p'):
        print("ID\tAmount\tDate\tCategory")
        for data in datall:
            print(str(data[idname])+"\t$"+str(data['amount'])+"\t"+str(data['date'])[0:10]+"\t"+data['category'])
    #if the table is neither print all columns
    else:
        print("ID\tAmount\tDate\t\tCategory\t\tDescription")
        for data in datall:
            print(str(data[idname])+"\t$"+str(data['amount'])+"\t"+str(data['date'])[0:10]+"\t"+data['category']+getTabs(data['category'])+data['description'])

#Add a datapoint to the given table
def add(table):
    url="http://"+serverloc.serverloc+"/api/"+table
    print("Adding new item to "+table)
    amount=int(input('Item cost: '))
    description=""
    if(table[0]!='p'):
        description=input('Item description: ')
    print("Please select one of the following categories")
    catStr=""
    i=0
    for cat in categories:
       catStr=catStr+", ("+str(i)+") "+cat
       i+=1
    catStr=catStr[2:]
    print(catStr)
    catSel=int(input("Selection: "))
    confirmed=False;
    #Confirm that the appropriate category was selected and repeat query until user is satisfied
    while(not confirmed):
        confStr=input("You have selected "+categories[catSel]+" is this correct? (Y/n)")
        if(confStr=='y' or confStr=='Y'):
            confirmed=True
        else:
            catSel=int(input("Please select a category: "))
    catFin=categories[catSel]
    try:
        details=urllib.parse.urlencode({'category': catFin, 'amount': amount, 'description': description, 'date': datetime.date.today()})
        details=details.encode('utf8')
        urlr=urllib.request.Request(url, details)
        urlr.add_header("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.29 Safari/525.13")
        responseData=urllib.request.urlopen(urlr).read().decode('utf8', 'ignore')
        responseFail=False
    except urllib.error.HTTPError as e:
        responseDAta=e.read.decode('utf8', 'ignore')
        responseFail=True
    except urllib.error.URLError:
        responseFail=True
    except UnicodeEncodeError:
        responseFail=True
    
#update the datapoint with given id in given table
def update(table, upid):
    url="http://"+serverloc.serverloc+"/api/"+table
    urlRead=url+"?id="+upid
    print(urlRead)
    response=urllib.request.urlopen(urlRead)
    #Convert the call to json and then get the data element from the json data
    jsonres=json.loads(response.read().decode(response.info().get_param('charset') or 'utf-8'))
    data=jsonres['data'][0]
    print("Leave blank to leave unchanged")
    newAmount=input("Amount ("+str(data['amount'])+"): ")
    if(table[0]!='p'):
        newDesc=input("Description ("+data['description']+"): ")
    print("Please select one of the following categories")
    catStr=""
    i=0
    for cat in categories:
       catStr=catStr+", ("+str(i)+") "+cat
       i+=1
    catStr=catStr[2:]
    print(catStr)
    catSel=input("Selection: ")
    catFin=""
    if(catSel):
        catSel=int(catSel)
        confirmed=False;
        #Confirm that the appropriate category was selected and repeat query until user is satisfied
        while(not confirmed):
            confStr=input("You have selected "+categories[catSel]+" is this correct? (Y/n)")
            if(confStr=='y' or confStr=='Y'):
                confirmed=True
            else:
                catSel=int(input("Please select a category: "))
        catFin=categories[catSel]
    if(table[0]!='m'):
        newDate=input("Date ("+str(data['date'])+"): ")
    details={'id': upid}
    if(newAmount):
        details['amount']=newAmount
    if(newDesc):
        details['description']=newDesc
    if(catFin):
        details['category']=catFin
    if(newDate):
        newDateT=time.strptime(newDate, "%y-%m-%d")
        details['date']=newDateT
    details=urllib.parse.urlencode(details)
    details=details.encode('utf8')
    urlr=urllib.request.Request(url, details)
    urlr.get_method=lambda: 'PUT'
    responseData=urllib.request.urlopen(urlr).read().decode('utf8', 'ignore')

#Delete the datapoint with given id in given table
def delete(table, delid):
    url="http://"+serverloc.serverloc+"/api/"+table
    
#try to iterate over all args using getopt to split the data into operations and arguments given by the following lists
try:
    opts, args = getopt.getopt(sys.argv[1:], 'hlu:ad:s:t:', ["list", "update=", "add", "delete=", "sort=", "table="])
except getopt.GetoptError:
    #An error occured, print out proper usage and exit
    usage()
    sys.exit(2)

#Set some variables to prepare for iterating over the arguments
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

#Do operations listed by arguments
if listem:
    lister(sort, table)
if addem:
    add(table)
if updatem:
    update(table, updateid)
if deletem:
    delete(table, deleteid)
