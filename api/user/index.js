const mysql= require('mysql');
const express=require('express');
var app=express();
const jwt = require('jsonwebtoken');
const SecretKey ='K27eEaz9sKC7cFnIBjxY4i0tucCTdmgN6Oe-iMv7Lsjcu0wo_coM_tnYekUM8HDUbWjuCpaJLrF5uLDx680NAQ';
const HeaderKey = "Pz6WbvhZAQGsUtAxRJK3vtXCrJDW6kb3yMwtnGKu2kpfT9RahulGaurqFWfvFptqftcF87mBbV7pJWmPCPR5fZentc3qQVTtGLbqbjvGquT5B8UT2Kvjk7BCUm7hqtkqmJ3yR6fMFdWkWwvRGjrtSZjs52TdKC5Xazvp6b22pKNQSybvNb4mAwwuzXQFLKM7Pq5htpNNg8ZJ9dZJUF8gqc3aFXywYvaFLMXWdNUfErL8GEgUR3sEpNajEXbUcL22";
const bodyparser=require('body-parser');
var multer  = require('multer');
const verify = require('jsonwebtoken/verify');
var upload = multer();
var uuid=require('uuid');

app.use(bodyparser.json());
app.use(bodyparser.urlencoded({ extended: true }));




var mysqlConnection= mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'',
	database:'blogair_db'
	
});

//connect to database
mysqlConnection.connect((err)=>{
	if(!err)
		console.log('DB connecton status: Connected sucessfully');
	else
		console.log('DB connecton status: Failed to connect '+ JSON.stringify(err,undefined,2));
});

app.listen(3000,()=>console.log('express server is running at port no : 3000'),'192.168.4.159');


//random uid test
app.get('/randomuid',(req,res)=>{
	var random_num= uuid.v4();
	res.send(random_num);
});


//get all users
app.get('/user',(req,res)=>{
	mysqlConnection.query('SELECT * FROM user',(err,rows,fields)=>{
		if(!err){
			//o[key].push(rows);
			
			res.send(rows);
		}
		else
			console.log(err);
	})
});



//test text
app.get('/testext',(req,res)=>{
	res.send("this is a sample text from api");
	}
	
	);
	

//get specific user
app.get('/user/:uid',(req,res)=>{
	mysqlConnection.query('SELECT * FROM user where uid= ? ',[req.params.uid],(err,rows,fields)=>{
		if(!err)
			res.send(rows);
		else
			console.log(err);
	})
});


//delete a user with provided uid
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

//register a user pass full_name, email_id, password, uid
app.post('/user/register',upload.none(),verifyHeader, async (req,res) =>{

	if(req.key==HeaderKey){
		var data=req.body;
		var full_name= data.full_name;
		var email_id=data.email_id;
		var password=data.password;
		var uid=data.uid;

		//change this while live server
		var thumb_image='https://toppng.com/uploads/preview/file-svg-user-icon-material-desi-11563317072p2p27gjccw.png';
		console.log('register request: ');
		console.log('full_naame:  ', full_name);
		console.log('email_id:  ',email_id);
		console.log('password:  ',password);
		
		mysqlConnection.query('insert into user(uid,email_id,full_name,password) values (?,?,?,?)' ,[uid,email_id,full_name,password], async function(err,rows) {
			if(!err){
				const user ={
					id: uid,
					email_id:email_id,
					full_name:full_name
				}

				jwt.sign({user :user}, SecretKey, async (err, token)=>{
					if(!!err){
						console.log('error creating token ', err);
							const obj = [{
								message: 'register failed',
								error: true,
								token: null
							}];
							res.status(400).send(obj);
					}
					else{
						const obj ={
							message :'register success',
							error: false,
							token: token,
							name: full_name,
							thumb_image:thumb_image,
							uid: uid
						};
					//change this to blogair user while live server
					var fid=uuid.v4();
						mysqlConnection.query('insert into follow(fid,follower_uid,following_uid ) values(?,?,"1") ' , [fid,uid], async function(err,rows){
							if(!err){
								console.log('followed user 1 successfully');
								
							}
							else{
								console.log('error while following 1: ',err);
							}
						} )  
						
						console.log("----------------------");
						console.log(obj);
						console.log("----------------------");
						res.status(200).json(obj);
					}

				})

			}
			else{
					if(err.code == 'ER_DUP_ENTRY'){
						res.status(406).send();
						console.log('error in register:  ',err.code);
					}
					console.log('error while register:  ',err)
				
			}
		} )
	

	}
	else {
        res.status(400).send();
    }

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
app.get('/user/profile/posts/:uid',verifyHeader,verifyToken,(req,res)=>{

	jwt.verify(req.token ,SecretKey, async function(err,authData){
	if(!!err){
		res.sendStatus(400);
	}
	else{
		const auth_uid= authData.user.id;
		if(auth_uid==req.params.uid){
			mysqlConnection.query('SELECT pid,post_image FROM post where uid= ? ',[req.params.uid],(err,rows,fields)=>{
				if(!err){
					console.log('account posts retireve successfully');
					res.send(rows);
				}
				else{
					console.log(err);

				}
			})
		}
		else{
			console.log('User not authorized');
		}
	
	}



})
	
});



//get user home_feed pass current_user_uid and page_no
app.get('/user/homeFeed/:page_no',verifyHeader, verifyToken,(req,res)=>{
	jwt.verify(req.token, SecretKey, async function (err,authData) {
		if(!!err){
			res.sendStatus(401);
		}
		else{
			const uid=authData.user.id;
			var start_limit;
			var end_limit;
			var page_no=req.params.page_no;
		
			if(page_no==1){
				start_limit=0;
				end_limit=5;
			}else{
				start_limit=(page_no -1 ) * 5;
				end_limit=start_limit + 5;
			}
			
			mysqlConnection.query('select u.full_name, u.thumb_image,p.* from user u JOIN post p on p.uid=u.uid WHERE u.uid in (SELECT uid FROM post where uid = ? OR uid in (select following_uid from follow WHERE `follower_uid` = ?) ORDER BY p.time_stamp desc ) ORDER BY `p`.`time_stamp` DESC limit ?,?',[uid,uid,start_limit,end_limit],(err,rows,fields)=>{
				if (!rows || rows == null || rows === null || rows[0] === null || !rows[0]) {
					//No more posts
					console.log('uid passed with error:  ',uid);
					console.log('page_no passed with error:  ',page_no);

					console.log('Error while getting posts: ', err);
					res.json(null);
				}
				else{
					console.log('uid passed: ',uid);
					res.status(200).send(rows);
				}
			})
		}
	})
});





//login User
app.post('/user/login',upload.none(),verifyHeader, async (req,res)=>{

	if(req.key == HeaderKey){
		var data =req.body;
		var email_id= data.email_id;
		var password =data.password;
		console.log('req:  ',req.body);
		console.log('email:  ', email_id);
		console.log('password:  ',password);
		console.log("header key: ",req.key)
		mysqlConnection.query('select full_name,uid,password, thumb_image from user where email_id = ?' , [email_id] , async function(err, rows) {
	
			if(!!err){
				console.log('error in email check query');
			var obj ={
				message: 'Server error , please try again',
				error: true
			}
			res.status(502).json(obj);
			}
			else{
				if (rows.length == 0) {
					var obj = {
						message: 'No user found with this email',
						error: true
					}
					res.status(401).json(obj)
				}
	
				else{
					console.log("----------------------");
					console.log("User login querry result ",rows);
					console.log("----------------------");
					try{
						if (password== rows[0].password){
							const user ={
								id: rows[0].uid,
								email_id:email_id,
								full_name:rows[0].full_name
							}
	
							jwt.sign({user :user}, SecretKey, async (err, token)=>{
								if(!!err){
									console.log('error creating token ', err);
	
										const obj = [{
											message: 'Login failed',
											error: true,
											token: null
										}];
										res.status(400).send(obj);
								}
								else{
									const obj ={
										message :'login success',
										error: false,
										token: token,
										name: rows[0].full_name,
										thumb_image:rows[0].thumb_image,
										uid: rows[0].uid
									};
	
	
									console.log("----------------------");
									console.log(obj);
									console.log("----------------------");
									res.status(200).json(obj);
								}
	
							})
	
						}
						else{
							var obj = {
								message: 'Invalid credentials',
								error: true
							}
							res.sendStatus(401);
						}
					}
					catch (e){
						console.log('exception in password check');
						var obj = {
							message: 'Server error, please try again',
							error: true
						}
						res.status(500).json(obj);
					}
				}
			}
	
		});
	}
	else {
        res.status(400).send();
    }
});



//verify login
app.get('/user/login/test' , verifyHeader, verifyToken, async function (req,res)  {
	jwt.verify(req.token,SecretKey, async function (err, authData)  {
		if(!!err){
			res.sendStatus(401);
		}
		else{
			const msg={
				msg:"user is verified",
				uid: authData.user.id,
				full_name: authData.user.full_name,
				email_id: authData.user.email_id
			}
			
			res.status(200).json(msg);
		}
	})

});




//verify header
function verifyHeader(req, res, next) {
    //get auth header value
    const keyHeader = req.headers['key'];
    // check if not is undefined

    if (typeof keyHeader !== 'undefined') {
        req.key = keyHeader;

        if (keyHeader == HeaderKey) {
            //next middleware
            next();
        } else {
            // forbidden
            console.log('forbidden 2');
            res.sendStatus(403).send();
        }

    } else {
        // forbidden
        console.log('forbidden 1');
        res.sendStatus(403).send();
    }
}

//verify token
function verifyToken(req, res, next) {
    //get auth header value
    const bearerHeader = req.headers['authorization'];
    // check if not is undefined

    if (typeof bearerHeader !== 'undefined') {
        //spilit at the space
        const bearer = bearerHeader.split(' ');
        //get token from array
		const bearerToken = bearer[1];
		console.log('barrer token: ', bearerToken);
        //set the token
        req.token = bearerToken;
        //next middleware
        next();
    } else {
        // forbidden
        res.sendStatus(403).send();
    }
}