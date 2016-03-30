//require express and set up express/handlebars for main workload
var express = require('express');
var app=express();
//set deafult layout to main and add support for sections
var handlebars = require('express-handlebars').create({ defaultLayout:'main' , helpers: {
    section: function(name, options){
	if(!this._sections) this._sections={};
	this._sections[name]=options.fn(this);
	return null;
    }
}});
var mysql=require('mysql');
var credentials=require('./credentials.js');
connectionpool=mysql.createPool({
    host: credentials.database.server,
    user: credentials.database.user,
    password: credentials.database.pass,
    database: 'budget'
})
//add body parser to get body for post requests to api
var bodyParser=require('body-parser');
app.use(bodyParser.urlencoded());
//use handlebars for templating
app.engine('handlebars', handlebars.engine);
//set public to usable
app.use(express.static(__dirname + '/public'));
//allow for tests to be show
app.use(function(req, res, next){
    res.locals.showTests=app.get('env') !== 'production' && req.query.test=='1';
    next();
});
//set view engine to handlebars
app.set('view engine', 'handlebars');
//set port to 3420
app.set('port', process.env.PORT || 3420);

//get api for all tables, include option to select by time or by category
app.get('/api/expenses', function(req, res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var connectionStr='SELECT * FROM `expenses` WHERE 1';
	    if(req.query['id']){
		connectionStr=connectionStr+' and expense_id='+req.query['id'];
	    }
	    else{
		 if(req.query['category']){
		     connectionStr=connectionStr+' and category=\''+req.query['category']+'\'';
		 }
		if(req.query['datefrom']&&req.query['dateto']){
		    connectionStr=connectionStr+' and date BETWEEN \''+req.query['datefrom']+'\' AND \''+req.query['dateto']+'\'';
		}
	    }
	    if(req.query['sortby']){
		connectionStr=connectionStr+" ORDER BY "+req.query['sortby']+" DESC";
	    }
	    else{
		connectionStr=connectionStr+" ORDER BY expense_id DESC";
	    }
	    connection.query(connectionStr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    result: 'success',
		    data: rows
		});
		connection.release();
	    });
	}
    });
});

//get incomes
app.get('/api/incomes', function(req, res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var connectionStr='SELECT * FROM `incomes` WHERE 1';
	    if(req.query['id']){
		connectionStr=connectionStr+' and income_id='+req.query['id'];
	    }
	    else{
		 if(req.query['category']){
		     connectionStr=connectionStr+' and category=\''+req.query['category']+'\'';
		 }
		if(req.query['datefrom']&&req.query['dateto']){
		    connectionStr=connectionStr+' and date BETWEEN \''+req.query['datefrom']+'\' AND \''+req.query['dateto']+'\'';
		}
	    }
	    if(req.query['sortby']){
		connectionStr=connectionStr+" ORDER BY "+req.query['sortby']+" DESC";
	    }
	    else{
		connectionStr=connectionStr+" ORDER BY income_id DESC";
	    }
	    connection.query(connectionStr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    result: 'success',
		    data: rows
		});
		connection.release();
	    });
	}
    });
});

//get monthly expenses
app.get('/api/monthlyexpenses', function(req, res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var connectionStr='SELECT * FROM `monthly_expenses` WHERE 1';
	    if(req.query['id']){
		connectionStr=connectionStr+' and monthly_expense_id='+req.query['id'];
	    }
	    else{
		 if(req.query['category']){
		     connectionStr=connectionStr+' and category=\''+req.query['category']+'\'';
		 }
	    }
	    if(req.query['sortby']){
		connectionStr=connectionStr+" ORDER BY "+req.query['sortby']+" DESC";
	    }
	    else{
		connectionStr=connectionStr+" ORDER BY monthly_expense_id DESC";
	    }
	    connection.query(connectionStr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    result: 'success',
		    data: rows
		});
		connection.release();
	    });
	}
    });
});

//get monthly incomes
app.get('/api/monthlyincomes', function(req, res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var connectionStr='SELECT * FROM `monthly_incomes` WHERE 1';
	    if(req.query['id']){
		connectionStr=connectionStr+' and monthly_income_id='+req.query['id'];
	    }
	    else{
		 if(req.query['category']){
		     connectionStr=connectionStr+' and category=\''+req.query['category']+'\'';
		 }
	    }
	    if(req.query['sortby']){
		connectionStr=connectionStr+" ORDER BY "+req.query['sortby']+" DESC";
	    }
	    else{
		connectionStr=connectionStr+" ORDER BY monthly_income_id DESC";
	    }
	    connection.query(connectionStr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    result: 'success',
		    data: rows
		});
		connection.release();
	    });
	}
    });
});

//get expenses plan
app.get('/api/planexpenses', function(req, res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var connectionStr='SELECT * FROM `expense_plan` WHERE 1';
	    if(req.query['id']){
		connectionStr=connectionStr+' and expense_id='+req.query['id'];
	    }
	    else{
		 if(req.query['category']){
		     connectionStr=connectionStr+' and category=\''+req.query['category']+'\'';
		 }
		if(req.query['datefrom']&&req.query['dateto']){
		    connectionStr=connectionStr+' and date BETWEEN \''+req.query['datefrom']+'\' AND \''+req.query['dateto']+'\'';
		}
	    }
	    if(req.query['sortby']){
		connectionStr=connectionStr+" ORDER BY "+req.query['sortby']+" DESC";
	    }
	    else{
		connectionStr=connectionStr+" ORDER BY expense_id DESC";
	    }
	    connection.query(connectionStr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    result: 'success',
		    data: rows
		});
		connection.release();
	    });
	}
    });
});

//get income plans
app.get('/api/planincomes', function(req, res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var connectionStr='SELECT * FROM `income_plan` WHERE 1';
	    if(req.query['id']){
		connectionStr=connectionStr+' and income_id='+req.query['id'];
	    }
	    else{
		 if(req.query['category']){
		     connectionStr=connectionStr+' and category=\''+req.query['category']+'\'';
		 }
		if(req.query['datefrom']&&req.query['dateto']){
		    connectionStr=connectionStr+' and date BETWEEN \''+req.query['datefrom']+'\' AND \''+req.query['dateto']+'\'';
		}
	    }
	    if(req.query['sortby']){
		connectionStr=connectionStr+" ORDER BY "+req.query['sortby']+" DESC";
	    }
	    else{
		connectionStr=connectionStr+" ORDER BY income_id DESC";
	    }
	    connection.query(connectionStr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    result: 'success',
		    data: rows
		});
		connection.release();
	    });
	}
    });
});

//add using post
app.post('/api/expenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var idquery="SELECT * FROM `expenses` ORDER BY expense_id DESC LIMIT 1";
	    connection.query(idquery, function(err, row, fields){
		if(err){
		    console.log(err);
		    res.status(500);
		    res.render('500');
		}
		var id=(row[0]['expense_id'])+1;
		var querystr="INSERT INTO `expenses` (`expense_id`, `category`, `date`, `amount`, `description`) VALUES ("+id+", \'"+req.body.category+"\', \'"+req.body.date+"\', "+req.body.amount+", \'"+req.body.description+"\')";
		connection.query(querystr, function(err, rows, fields){
		    if(err){
			console.error(err);
			res.status(500);
			res.render('500');
		    }
		    res.send({
			success: 'true'
		    });
		    connection.release();
		});
	    });
	}
    });
});

app.post('/api/incomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var idquery="SELECT * FROM `incomes` ORDER BY income_id DESC LIMIT 1";
	    connection.query(idquery, function(err, row, fields){
		if(err){
		    console.log(err);
		    res.status(500);
		    res.render('500');
		}
		var id=(row[0]['expense_id'])+1;
		var querystr="INSERT INTO `incomes` (`income_id`, `category`, `date`, `amount`, `description`) VALUES ("+id+", \'"+req.body.category+"\', \'"+req.body.date+"\', "+req.body.amount+", \'"+req.body.description+"\')";
		connection.query(querystr, function(err, rows, fields){
		    if(err){
			console.error(err);
			res.status(500);
			res.render('500');
		    }
		    res.send({
			success: 'true'
		    });
		    connection.release();
		});
	    });
	}
    });
});

app.post('/api/monthlyexpenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var idquery="SELECT * FROM `monthly_expenses` ORDER BY monthly_expense_id DESC LIMIT 1";
	    connection.query(idquery, function(err, row, fields){
		if(err){
		    console.log(err);
		    res.status(500);
		    res.render('500');
		}
		var id=(row[0]['expense_id'])+1;
		var querystr="INSERT INTO `monthly_expenses` (`monthly_expense_id`, `category`, `amount`, `description`) VALUES ("+id+", \'"+req.body.category+"\', "+req.body.amount+", \'"+req.body.description+"\')";
		connection.query(querystr, function(err, rows, fields){
		    if(err){
			console.error(err);
			res.status(500);
			res.render('500');
		    }
		    res.send({
			success: 'true'
		    });
		    connection.release();
		});
	    });
	}
    });
});

app.post('/api/monthlyincomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var idquery="SELECT * FROM `monthly_incomes` ORDER BY monthly_income_id DESC LIMIT 1";
	    connection.query(idquery, function(err, row, fields){
		if(err){
		    console.log(err);
		    res.status(500);
		    res.render('500');
		}
		var id=(row[0]['expense_id'])+1;
		var querystr="INSERT INTO `monthly_incomes` (`monthly_income_id`, `category`, `amount`, `description`) VALUES ("+id+", \'"+req.body.category+"\', "+req.body.amount+", \'"+req.body.description+"\')";
		connection.query(querystr, function(err, rows, fields){
		    if(err){
			console.error(err);
			res.status(500);
			res.render('500');
		    }
		    res.send({
			success: 'true'
		    });
		    connection.release();
		});
	    });
	}
    });
});


app.post('/api/planexpenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var idquery="SELECT * FROM `expense_plan` ORDER BY expense_id DESC LIMIT 1";
	    connection.query(idquery, function(err, row, fields){
		if(err){
		    console.log(err);
		    res.status(500);
		    res.render('500');
		}
		var id=(row[0]['expense_id'])+1;
		var querystr="INSERT INTO `expense_plan` (`expense_id`, `category`, `date`, `amount`, `description`) VALUES ("+id+", \'"+req.body.category+"\', \'"+req.body.date+"\', "+req.body.amount+", \'"+req.body.description+"\')";
		connection.query(querystr, function(err, rows, fields){
		    if(err){
			console.error(err);
			res.status(500);
			res.render('500');
		    }
		    res.send({
			success: 'true'
		    });
		    connection.release();
		});
	    });
	}
    });
});

app.post('/api/planincomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var idquery="SELECT * FROM `income_plan` ORDER BY income_id DESC LIMIT 1";
	    connection.query(idquery, function(err, row, fields){
		if(err){
		    console.log(err);
		    res.status(500);
		    res.render('500');
		}
		var id=(row[0]['expense_id'])+1;
		var querystr="INSERT INTO `income_plan` (`income_id`, `category`, `date`, `amount`, `description`) VALUES ("+id+", \'"+req.body.category+"\', \'"+req.body.date+"\', "+req.body.amount+", \'"+req.body.description+"\')";
		connection.query(querystr, function(err, rows, fields){
		    if(err){
			console.error(err);
			res.status(500);
			res.render('500');
		    }
		    res.send({
			success: 'true'
		    });
		    connection.release();
		});
	    });
	}
    });
});


//update entries using put
app.put('/api/expenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="UPDATE `expenses` SET";
	    var first=true;
	    if(req.body.category){
		if(first){
		    first=false;
		    querystr=querystr+" category=\'"+req.body.category+"\'";
		}
		else{
		    querystr=querystr+", category=\'"+req.body.category+"\'";
		}
	    }
	    if(req.body.date){
		if(first){
		    first=false;
		    querystr=querystr+" date=\'"+req.body.date+"\'";
		}
		else{
		    querystr=querystr+", date=\'"+req.body.date+"\'";
		}
	    }
	    if(req.body.amount){
		if(first){
		    first=false;
		    querystr=querystr+" amount="+req.body.amount;
		}
		else{
		    querystr=querystr+", amount="+req.body.amount;
		}
	    }
	    if(req.body.description){
		if(first){
		    first=false;
		    querystr=querystr+" description=\'"+req.body.description+"\'";
		}
		else{
		    querystr=querystr+", description=\'"+req.body.description+"\'";
		}
	    }
	    querystr=querystr+" WHERE expense_id="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send(
		    {success: 'true'}
		);
		connection.release();
	    });
	}
    });
});

app.put('/api/incomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="UPDATE `incomes` SET";
	    var first=true;
	    if(req.body.category){
		if(first){
		    first=false;
		    querystr=querystr+" category=\'"+req.body.category+"\'";
		}
		else{
		    querystr=querystr+", category=\'"+req.body.category+"\'";
		}
	    }
	    if(req.body.date){
		if(first){
		    first=false;
		    querystr=querystr+" date=\'"+req.body.date+"\'";
		}
		else{
		    querystr=querystr+", date=\'"+req.body.date+"\'";
		}
	    }
	    if(req.body.amount){
		if(first){
		    first=false;
		    querystr=querystr+" amount="+req.body.amount;
		}
		else{
		    querystr=querystr+", amount="+req.body.amount;
		}
	    }
	    if(req.body.description){
		if(first){
		    first=false;
		    querystr=querystr+" description=\'"+req.body.description+"\'";
		}
		else{
		    querystr=querystr+", description=\'"+req.body.description+"\'";
		}
	    }
	    querystr=querystr+" WHERE income_id="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.put('/api/monthlyexpenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="UPDATE `monthly_expenses` SET";
	    var first=true;
	    if(req.body.category){
		if(first){
		    first=false;
		    querystr=querystr+" category=\'"+req.body.category+"\'";
		}
		else{
		    querystr=querystr+", category=\'"+req.body.category+"\'";
		}
	    }
	    if(req.body.amount){
		if(first){
		    first=false;
		    querystr=querystr+" amount="+req.body.amount;
		}
		else{
		    querystr=querystr+", amount="+req.body.amount;
		}
	    }
	    if(req.body.description){
		if(first){
		    first=false;
		    querystr=querystr+" description=\'"+req.body.description+"\'";
		}
		else{
		    querystr=querystr+", description=\'"+req.body.description+"\'";
		}
	    }
	    querystr=querystr+" WHERE monthly_expense_id="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.put('/api/monthlyincomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="UPDATE `monthly_incomes` SET";
	    var first=true;
	    if(req.body.category){
		if(first){
		    first=false;
		    querystr=querystr+" category=\'"+req.body.category+"\'";
		}
		else{
		    querystr=querystr+", category=\'"+req.body.category+"\'";
		}
	    }
	    if(req.body.amount){
		if(first){
		    first=false;
		    querystr=querystr+" amount="+req.body.amount;
		}
		else{
		    querystr=querystr+", amount="+req.body.amount;
		}
	    }
	    if(req.body.description){
		if(first){
		    first=false;
		    querystr=querystr+" description=\'"+req.body.description+"\'";
		}
		else{
		    querystr=querystr+", description=\'"+req.body.description+"\'";
		}
	    }
	    querystr=querystr+" WHERE monthly_income_id="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.put('/api/planexpenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="UPDATE `expense_plan` SET";
	    var first=true;
	    if(req.body.category){
		if(first){
		    first=false;
		    querystr=querystr+" category=\'"+req.body.category+"\'";
		}
		else{
		    querystr=querystr+", category=\'"+req.body.category+"\'";
		}
	    }
	    if(req.body.date){
		if(first){
		    first=false;
		    querystr=querystr+" date=\'"+req.body.date+"\'";
		}
		else{
		    querystr=querystr+", date=\'"+req.body.date+"\'";
		}
	    }
	    if(req.body.amount){
		if(first){
		    first=false;
		    querystr=querystr+" amount="+req.body.amount;
		}
		else{
		    querystr=querystr+", amount="+req.body.amount;
		}
	    }
	    querystr=querystr+" WHERE expense_id="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.put('/api/planincomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="UPDATE `income_plan` SET";
	    var first=true;
	    if(req.body.category){
		if(first){
		    first=false;
		    querystr=querystr+" category=\'"+req.body.category+"\'";
		}
		else{
		    querystr=querystr+", category=\'"+req.body.category+"\'";
		}
	    }
	    if(req.body.date){
		if(first){
		    first=false;
		    querystr=querystr+" date=\'"+req.body.date+"\'";
		}
		else{
		    querystr=querystr+", date=\'"+req.body.date+"\'";
		}
	    }
	    if(req.body.amount){
		if(first){
		    first=false;
		    querystr=querystr+" amount="+req.body.amount;
		}
		else{
		    querystr=querystr+", amount="+req.body.amount;
		}
	    }
	    querystr=querystr+" WHERE income_id="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

//delete entries
app.delete('/api/expenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="DELETE FROM `expenses` WHERE `expense_id`="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.delete('/api/incomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="DELETE FROM `incomes` WHERE `income_id`="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.delete('/api/monthlyexpenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="DELETE FROM `monthly_expenses` WHERE `monthly_expense_id`="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.delete('/api/monthlyincomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="DELETE FROM `monthly_incomes` WHERE `monthly_income_id`="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.delete('/api/planexpenses', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="DELETE FROM `expense_plan` WHERE `expense_id`="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    succses: 'true'
		});
		connection.release();
	    });
	}
    });
});

app.delete('/api/planincomes', function(req,res){
    connectionpool.getConnection(function(err, connection){
	if(err){
	    console.error('CONNECTION error: ', err);
	    res.statusCode=503;
	    res.send({
		result: 'error',
		err: err.code
	    });
	}
	else{
	    var querystr="DELETE FROM `income_plan` WHERE `income_id`="+req.body.id;
	    connection.query(querystr, function(err, rows, fields){
		if(err){
		    console.error(err);
		    res.status(500);
		    res.render('500');
		}
		res.send({
		    success: 'true'
		});
		connection.release();
	    });
	}
    });
});


app.use(function(req, res){
    res.status(404);
    res.render('404', {layout: false});
});

app.use(function(err, req, res, next){
    console.error(err.stack);
    res.status(500);
    res.render('500', {layout: false});
});

app.listen(app.get('port'), function(){
 console.log( 'Express started on http://localhost:' +
 app.get('port') + '; press Ctrl-C to terminate.' );
});
