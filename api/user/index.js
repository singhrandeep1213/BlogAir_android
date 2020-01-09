const mysql= require('mysql');
const express=require('express');
var app=express();
const bodyparser=require('body-parser');

app.use(bodyparser.json());

var mysqlConnection= mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'',
	database:'blogair_db'
	
});

mysqlConnection.connect((err)=>{
	if(!err)
		console.log('db conn success');
	else
		console.log('db conn failed\n error: '+ JSON.stringify(err,undefined,2));
});

app.listen(3000,()=>console.log('express server is running at port no : 3000'),'192.168.4.159');




//get all users
app.get('/user',(req,res)=>{
	mysqlConnection.query('SELECT * FROM user',(err,rows,fields)=>{
		if(!err)
			res.send(rows);
		else
			console.log(err);
	})
});





//get specific user
app.get('/user/:uid',(req,res)=>{
	mysqlConnection.query('SELECT * FROM user where uid= ? ',[req.params.uid],(err,rows,fields)=>{
		if(!err)
			res.send(rows);
		else
			console.log(err);
	})
});


//delete a user- uid will be added to http url localhost:3000/user/1
app.delete('/user/:uid',(req,res)=>{
	mysqlConnection.query('delete from user WHERE uid= ?',[req.params.uid],(err,rows,fields)=>{
		if(!err)
			res.send('Deleted seccessfully');
		else
			console.log(err);
	})
});

//insert and update  a user
app.post('/user',(req,res)=>{
		let usr=req.body;
		 var sql = "CALL UserAddOrEdit(?,?,?,?,?,?,?);"; 
		mysqlConnection.query(sql,[usr.uid,usr.email_id,usr.thumb_image,usr.full_name,usr.country,usr.dob,usr.time_stamp],(err,rows,fields)=>{
			if(!err)
				res.send('updated seccessfully');
			else
				console.log(err);
	})
});

//get all posts
app.get('/post',(req,res)=>{
	mysqlConnection.query('SELECT * FROM post',(err,rows,fields)=>{
		if(!err)
			res.send(rows);
		else
			console.log(err);
	})
});

 //get all posts of a user
app.get('/post/:uid',(req,res)=>{
	mysqlConnection.query('SELECT * FROM post where uid= ? ',[req.params.uid],(err,rows,fields)=>{
		if(!err)
			res.send(rows);
		else
			console.log(err);
	})
});



//get user home_feed pass current_user_uid
app.get('/user/homeFeed/:uid',(req,res)=>{
	mysqlConnection.query('select u.full_name, u.thumb_image,p.* from user u JOIN post p on p.uid=u.uid WHERE u.uid in (SELECT uid FROM post where uid = ? OR uid in (select following_uid from follow WHERE `follower_uid` = ?) ORDER BY time_stamp desc)',[req.params.uid,req.params.uid],(err,rows,fields)=>{
		if(!err)
			res.send(rows);
		else
			console.log(err);
	})
});


