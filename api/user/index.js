const mysql= require('mysql');
const express=require('express');
var app=express();
const jwt = require('jsonwebtoken');
const SecretKey ='K27eEaz9sKC7cFnIBjxY4i0tucCTdmgN6Oe-iMv7Lsjcu0wo_coM_tnYekUM8HDUbWjuCpaJLrF5uLDx680NAQ';
const HeaderKey = "Pz6WbvhZAQGsUtAxRJK3vtXCrJDW6kb3yMwtnGKu2kpfT9RahulGaurqFWfvFptqftcF87mBbV7pJWmPCPR5fZentc3qQVTtGLbqbjvGquT5B8UT2Kvjk7BCUm7hqtkqmJ3yR6fMFdWkWwvRGjrtSZjs52TdKC5Xazvp6b22pKNQSybvNb4mAwwuzXQFLKM7Pq5htpNNg8ZJ9dZJUF8gqc3aFXywYvaFLMXWdNUfErL8GEgUR3sEpNajEXbUcL22";
const bodyparser=require('body-parser');
var multer  = require('multer');
var upload = multer();
var uuid=require('uuid');
const path = require('path');
const fileSys = require('fs');

app.use(bodyparser.json());
app.use(bodyparser.urlencoded({ extended: true }));


//const baseURL = 'https://myblogs.harshitaapptech.com/myblog/api/v3'; //live
const baseURL = 'http://192.168.1.6:3000'; //local

//databse connection
var mysqlConnection= mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'',
	database:'blogair_db',
	charset : 'utf8mb4'
});

//post imgae storage
var storageProfile = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './public/post_images');
    },
    filename: function (req, file, cb) {
        cb(null, file.fieldname  + uuid.v4() +
            path.extname(file.originalname));
    }
});

const uploadPostImage = multer({
	 storage: storageProfile 
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

//add new post
app.post('/user/post/addnew',verifyHeader,verifyToken,  uploadPostImage.single('post_image'),  async(req,res)=>{

	jwt.verify(req.token, SecretKey,async function(err,authData){
		if (!!err) {
			console.log('error in posting:  ',err);
			res.sendStatus(401);
		}
		else{
			var data =req.body;
			var pid= data.pid;
			var post_desc= data.post_desc;
			var post_image_url=req.file.filename;
			var uid = authData.user.id;
			var post_heading = data.post_heading;
			console.log('post_imge_url:   ',post_image_url);
			console.log('error in else:  ',err);
			uploadPost(pid,post_desc,post_image_url,uid,post_heading).then(function (result){

				res.status(200).send(result);

			}).catch(function(error){	
				console.log("Promise Rejected ", error);
                res.status(400).send("Error " + error);
			});	
		}
	});
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



 //get all posts of a user (i.e. user profile)
app.get('/user/profile/posts/:uid',verifyHeader,verifyToken,(req,res)=>{

	jwt.verify(req.token ,SecretKey, async function(err,authData){
	if(!!err){
		res.sendStatus(401);
	}
	else{
		var token=req.token;
		const auth_uid= authData.user.id;
		if(auth_uid==req.params.uid){
			mysqlConnection.query('SELECT pid,post_image FROM post where uid= ? ',[req.params.uid],(err,rows,fields)=>{
				if(!err){
					
					Object.keys(rows).forEach(key => {
						var singlePost=rows[key]
						if (rows[key].post_image != null) {
							singlePost.post_image = baseURL + "/post/image/" + token + "/" + rows[key].post_image;
							console.log('post image:   ', rows[key].post_image);
						}
					
					});
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
			var start_limit=0;
			var data_limit=5;
			var page_no=req.params.page_no;
			var token=req.token;
			var allBlogs=[];
		
			if(page_no==1){
				start_limit=0;
				
			}else{
				start_limit=(page_no - 1) * 5 ; 
			}
			
			mysqlConnection.query('select u.full_name, u.thumb_image,p.* from user u JOIN post p on p.uid=u.uid WHERE u.uid in (SELECT uid FROM post where uid = ? OR uid in (select following_uid from follow WHERE `follower_uid` = ?) ORDER BY p.time_stamp desc ) ORDER BY `p`.`time_stamp` DESC limit ?,?',[uid,uid,start_limit,data_limit],(err,rows,fields)=>{
				if (!rows || rows == null || rows === null || rows[0] === null || !rows[0]) {
					//No more posts
					console.log('Error while getting posts: ', err);
					var obj = {
						message: "Posts_not_found",
						error: true,
						blog: []
					}

					res.status(200).send(obj)
				}
				else{

					var allPosts = []
                	var counter = 0;
                   	var exitCondition = rows.length - 1

					Object.keys(rows).forEach(key => {
						var singlePost=rows[key]
						if (rows[key].post_image != null) {
							singlePost.post_image = baseURL + "/post/image/" + token + "/" + rows[key].post_image;
							//console.log('post image:   ', rows[key].post_image);
						}
						current_pid= singlePost.pid;
						//console.log('current_pid:  ', rows[key].pid);
						//check if current user has bookmarked the post
						mysqlConnection.query('select count(bid) as is_bookmarked from bookmarks where uid = ? and pid = ?',[uid,current_pid], function(err,result) {
							if (!!err){
								//console.log('Error getting bookmarks:  ',err);
								var obj = {
									message: "Posts found",
									error: false,
									blog: allPosts
								}

								res.status(200).send(obj)
							}
							else{
								var is_bookmarked = result[0].is_bookmarked;
								//console.log('bookmark id:  ', is_bookmarked);
								singlePost.is_bookmarked=is_bookmarked;

								allBlogs.push(singlePost);

								var obj = {
									message: "Posts found",
									error: false,
									post: allBlogs
								}

								if (counter == exitCondition) {
									res.status(200).send(obj);
								}
								counter++;
								
								//console.log('singlePost:  ',singlePost);
								//console.log('rows:  ',rows);
								
							}
							
						})
						
					});
					//console.log('allblogs: ', allBlogs);
					//res.status(200).send(rows);
					//console.log('uid passed: ',uid);
					//console.log('rows:  ',rows);
					
				}
			})
		}
	})
});


//get single image url
app.get('/post/image/:token/:image_id', function(req,res){

	console.log('get worked');
	
	var token =req.params.token;
	console.log('token:  ',token);
	
	jwt.verify(token, SecretKey , (err,authData) => {
		if(!!err){
			console.log('user not verified ')
			res.sendStatus(401);
		}
		else{
			//user is verified
			var image_id = req.params.image_id;
			console.log('image id:  ',image_id);
			//return image file using file name present in database

			//change this when live
			//var filepath = __dirname + "/public/post_images/" + image_id;
			var filepath= 'D:/work/Android/BlogAir/api/user/public/post_images/' + image_id;
			console.log('filepath:  ',filepath);
			try{
				if(fileSys.existsSync(filepath)){
					res.sendFile(filepath);
				}
				else{
					console.log('error in if:  ');
					res.sendStatus(404);
				}
			}catch(err){
				console.log('getting error in try:  ',err);
				res.sendStatus(404);
			}
		}
		

	})

})




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


//upload image function
async function uploadPost(pid,post_desc,post_image_url,uid,post_heading){

	return new Promise(function(resolve,reject){

		mysqlConnection.query('INSERT INTO `post` (`pid`, `desc`, `post_image`, `uid`, `post_heading`) VALUES (?,?,?,?,?);' ,[pid,post_desc,post_image_url,uid,post_heading], async function(err,rows){
			if (!err) {
				var obj = {
					error: false,
					message: "Success"
				}
				resolve(obj);
			}
			else{
				var obj = {
					error: true,
					message: "Error: " + err
				}
				reject(obj);
			
			}

		});

	});

}