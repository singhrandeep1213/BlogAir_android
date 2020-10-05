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


//const baseURL = 'https://myblogs.harshitaapptech.com/'; //live
const baseURL = 'http://192.168.1.2:3000'; //local

//databse connection
var mysqlConnection= mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'',
	database:'blogair_db',
	charset : 'utf8mb4'
});

//profile imgae storage
var storageProfile = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './public/profile_images');
    },
    filename: function (req, file, cb) {
        cb(null, file.fieldname  + '_' + uuid.v4() +
            path.extname(file.originalname));
    }
});

//post imgae storage
var storagePost = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './public/post_images');
    },
    filename: function (req, file, cb) {
        cb(null, file.fieldname  +'_'+ uuid.v4() +
            path.extname(file.originalname));
    }
});



const uploadPostImage = multer({
	 storage: storagePost
	});

const uploadProfileImage =multer({
	storage: storageProfile
});

//connect to database
mysqlConnection.connect((err)=>{
	if(!err)
		console.log('DB connecton status: Connected sucessfully');
	else
		console.log('DB connecton status: Failed to connect '+ JSON.stringify(err,undefined,2));
});


//starting server
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

//add or edit name or user bio
app.post('/user/update/nameandbio',verifyHeader,verifyToken, upload.none(), async(req,res)=>{
	console.log('header: ', req.key);
	console.log('update token: ',req.token);

	jwt.verify(req.token,SecretKey, async function (err, authData){
		if(!!err){
			res.sendStatus(401);
		}
		else{
			var data= req.body;
			var full_name=data.full_name;
			var bio= data.bio;
			var uid= authData.user.id;
			console.log('updating uid:  ',uid);
			mysqlConnection.query('UPDATE `user` SET  full_name = ? , bio = ?   WHERE uid = ? ;',[full_name,bio,uid], async function(err,rows){

				if(!!err){
					var obj={
						message: 'failed',
						error: err
					}
					console.log('error in updating:  ',err);
					res.status(401).send(obj);
				}
				else{
					var obj={
						message: 'success',
						error: false
					}
					console.log('successfully updated');
					res.status(200).send(obj);
				}
			});		
		}
	}) 

});


//update user profile image
app.post('/user/update/thumbimage',verifyHeader,verifyToken,uploadProfileImage.single('profile_image'), async(req,res)=>{

	jwt.verify(req.token, SecretKey, async function(err,authData){
		if(err){
			console.log('error in uploading profile image: ', err);
		}
		else{
			var token=req.token;
			var thumb_image_url= req.file.filename;
			console.log('thumb_image_url: ',thumb_image_url);
			var uid = authData.user.id;
			uploadThumbImage(uid,thumb_image_url,token).then(function(result){
				res.status(200).send(result);
			}).catch(function(error){
				console.log('Promise rejected error: ',error);
			});
		}
	});


});


//change password from app
app.post('/user/update/password', verifyHeader, verifyToken, upload.none(), async(req,res)=>{

	jwt.verify(req.token,SecretKey, async function(err, authData){
		if(err){
			console.log('error in token: ', err);
			res.sendStatus(401);
		}
		else{
			var data= req.body;
			var old_password= data.old_password;
			var new_password= data.new_password;
			var uid= authData.user.id;
			mysqlConnection.query('select password from user where uid =?',[uid], async function(err, rows){
				if(err){
					console.log('error in query');
					res.sendStatus(401);
				}
				else{
					var current_password=rows[0].password;
					if(current_password == old_password){
					
						mysqlConnection.query('update user set password =? where uid = ?',[new_password,uid],async function(err,rows){

							if(err){
								console.log('failed to update new password');
								res,sendStatus(401);
							}
							else{
								var obj={
									error: false,
									message: 'new password successfully updated'
								}
								res.status(200).send(obj);
							}
						});

					}else{
						var obj={
							error: true,
							message: 'Old password does not match'
						}
						res.status(401).send(obj);
					}
				}


			})


		}


	});


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
		var thumb_image=baseURL + "/user/defaultthumbimage/" + 'default_thumb_image.png';
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



 //get all posts of current logged in user (i.e. user profile)
app.get('/user/current/profile/:uid',verifyHeader,verifyToken,(req,res)=>{

	jwt.verify(req.token ,SecretKey, async function(err,authData){
	if(!!err){
		res.sendStatus(401);
	}
	else{
		var token=req.token;
		const auth_uid= authData.user.id;
		var uid=req.params.uid; 
		if(auth_uid==uid){
			mysqlConnection.query('SELECT pid,post_image FROM post where uid= ? order by time_stamp desc',[uid],(err,rows,fields)=>{
				if(!err){
					var post=[];
					var following_count;
					var followers_count;
					Object.keys(rows).forEach(key => {
						var singlePost=rows[key]
						if (rows[key].post_image != null) {
							singlePost.post_image = baseURL + "/post/image/" + token + "/" + rows[key].post_image;
							console.log('post image:   ', rows[key].post_image);
						}
						post.push(singlePost)
					});


					//get following count
					mysqlConnection.query('select COUNT(following_uid) as following_count from follow WHERE follower_uid=?',[uid],(err, rows)=>{
						if(err){
							console.log('err in query 2');
							var obj = {
								error: true,
								message: "Error: " + err
							}
							res.status(401).send(obj);
						}
						else{
							following_count= rows[0].following_count;
							console.log('following count', following_count);
						}

					});

					//get followers count
					mysqlConnection.query('select COUNT(following_uid) as followers_count from follow WHERE following_uid=?',[uid],(err, rows)=>{
						if(err){
							console.log('err in query 2');
							var obj = {
								error: true,
								message: "Error: " + err
							}
							res.status(401).send(obj);
						
						}
						else{
							followers_count= rows[0].followers_count;
							console.log('followers count', followers_count);
							var obj= {
								error: false,
								message: 'current_user',
								type: 'current',
								following_count:following_count,
								followers_count:followers_count,
								post: post
							}
							res.status(200).send(obj);
						}
					});


				
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
							singlePost.thumb_image=baseURL + "/user/thumbimage/" + token + "/" + rows[key].thumb_image;
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
							    	console.log('current_pid :  ', rows[key].pid);
								//get likes count
								mysqlConnection.query('select count(lid) as likes_count, (select count(uid)  from likes where uid=? and pid=?) as is_liked_by_current_user from likes where pid=?',[uid,rows[key].pid,rows[key].pid], function(err,result){
									if(err){
										var obj = {
											message: "Posts found",
											error: false,
											blog: allPosts
										}
		
										res.status(200).send(obj)
									}
									else{
										console.log('uid: ',uid);
										var likes_count=result[0].likes_count;
										var is_liked_by_current_user= result[0].is_liked_by_current_user;
										console.log('is_liked_by_current_user:  ',is_liked_by_current_user );
										singlePost.likes_count= likes_count;
										singlePost.is_liked_by_current_user=is_liked_by_current_user;
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
										
									}

								});


							
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


//get user profile (not current) pass uid
app.get('/user/post/profile/:puid',verifyHeader,verifyToken,upload.none(), (req,res)=>{

	jwt.verify(req.token, SecretKey, (err, authData)=>{
		if(err){
			console.log('user not verified');
			res.status(401);
		}
		else{
			var current_user_id= authData.user.id;
			var passed_user_id= req.params.puid;
			var token=req.token;
			console.log('current user id: ', current_user_id);
			console.log('passed user id: ', passed_user_id);
			mysqlConnection.query('select following_uid from follow where follower_uid=? and following_uid = ? ', [current_user_id, passed_user_id], (err,rows)=>{
				if (!rows || rows == null || rows === null || rows[0] === null || !rows[0]) {
					//current user not following passed user
					console.log('not following: ');	

					//check if profile is public or not
					mysqlConnection.query('select is_public from user where uid=?',[passed_user_id],(err, rows)=>{
						if(err){
							console.log('error in query');
							res.sendStatus(401);
						}
						else{
							if (rows[0].is_public == 1){
								console.log('user is public');
								var message='user_is_public';
								var type='public';
								console.log('tokennn here : ',token);
								getUserProfile(passed_user_id,message,type,token).then(function(result){
									res.status(200).send(result);
								}).catch(function(error){
									console.log('promise rejected error: ', error);
								})
							}
							else{
								console.log('user is private');
								var type='private';
								var obj={
									error: false,
									message: 'user_is_private',
									type:type,
									following_count: 0,
									followers_count:0,
									post: []
								}
								res.status(200).send(obj);
							}
						}


					});
					//res.sendStatus(200);
				
				}
				else{
					console.log('following');
					var message='user_is_following';
					var type= 'following';
					getUserProfile(passed_user_id,message,type,token).then(function(result){
						res.status(200).send(result);
					}).catch(function(error){
						console.log('promise rejected error: ', error);
					})
					
					

				}

			});
		}
	});
	
});


//get single post image url
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


//get single profile imageurl
app.get('/user/thumbimage/:token/:image_id', function(req,res){

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
			var filepath= 'D:/work/Android/BlogAir/api/user/public/profile_images/' + image_id;
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

//get default user profile imageurl
app.get('/user/defaultthumbimage/:image_id',function(req,res){
	var image_id = req.params.image_id;
			console.log('image id:  ',image_id);
			//return image file using file name present in database

			//change this when live
			//var filepath = __dirname + "/public/post_images/" + image_id;
			var filepath= 'D:/work/Android/BlogAir/api/user/public/profile_images/' + image_id;
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

})

//get blocked users list
app.get('/user/blocked/users',verifyHeader, verifyToken,function(req,res){

	jwt.verify(req.token,SecretKey,async function(err, authData){

		if(err){
			res.sendStatus(401);
		}
		else{
			console.log('user verified: ');
			var uid=authData.user.id;
			mysqlConnection.query('SELECT u.full_name, u.thumb_image, u.uid from blocked_users b inner join user u on b.blocked_uid = u.uid where blocker_uid=?',[uid],async function(err,rows){
				if	(err){
					console.log('error while getting: ', err);
				}
				if(!rows || rows == null || rows === null || rows[0] === null || !rows[0]){
					console.log('error here1');
					//no user found
					var obj={
						error: false,
						message: 'no user found',
						blocked_users: []
					}
					res.status(200).send(obj);
				}
				else{
					var token=req.token;
					console.log('error here2');
					var allUsers = []
                	var counter = 0;
                   	var exitCondition = rows.length - 1

					Object.keys(rows).forEach(key => {
						var singleUser=rows[key];
						if (rows[key].thumb_image != null) {
							singleUser.thumb_image=baseURL + "/user/thumbimage/" + token + "/" + rows[key].thumb_image;
							//console.log('post image:   ', rows[key].post_image);
						}
						allUsers.push(singleUser);
						var obj={
							error: false,
							message: 'user found',
							blocked_users: allUsers
						}
						if (counter == exitCondition) {
							res.status(200).send(obj);
						}
						counter++;
					});
					
				}

			});
		}


	});



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
		mysqlConnection.query('select full_name,uid,password, thumb_image, bio from user where email_id = ?' , [email_id] , async function(err, rows) {
	
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
									var thumb_image = baseURL + "/user/thumbimage/" + token + "/" + rows[0].thumb_image;
									const obj ={
										message :'login success',
										error: false,
										token: token,
										name: rows[0].full_name,
										thumb_image:thumb_image,
										uid: rows[0].uid,
										bio: rows[0].bio
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


//like a post
app.post('/user/like/post', verifyHeader, verifyToken, upload.none(), async (req,res)=>{
	

	jwt.verify(req.token, SecretKey, async function(err,authData){

		if(err){
			console.log('user not authorized: ', err);
			res.sendStatus(401);
		}
		else{
			var data= req.body;
			var lid= data.lid;
			var pid= data.pid;
			var uid= authData.user.id;
			mysqlConnection.query('insert into likes(lid,pid,uid) values(?,?,?)', [lid,pid,uid] , async function(err, result){
				if(err){
					var obj ={
						error: true,
						message: 'error_in_like'
					}
					console.log('error in like: ', err);
					res.status(500).send(obj);
				}
				else{
					var obj={
						error: false,
						message :'successfully_liked'
					}
					console.log('like successful');
					res.status(200).send(obj);
				}

			});


		}

	});

});


//unlike a post
app.post('/user/post/unlike', verifyHeader,verifyToken, upload.none(), async(req,res)=>{

	jwt.verify(req.token, SecretKey, async function(err, authData){

		if(err){
			console.log('user not verified');
			res.status(401);
		}
		else{
			var data= req.body;
			var pid= data.pid;
			var uid= authData.user.id;

			mysqlConnection.query('delete from likes where pid=? AND uid =? ', [pid,uid],async function(err,rows){
				if(err){
					var obj={
						error: true,
						message: 'unable to dislike the post'
					};
					res.status(500).send(obj);
				}
				else{
					console.log('unliked successful');
					var obj={
						error: false,
						message: 'uliked_successfully'
					};
					res.status(200).send(obj);
				}
			} )

		}

	});

});

//bookmark a post
app.post('/user/post/bookmark', verifyHeader,verifyToken, upload.none(), async(req,res)=>{

	jwt.verify(req.token, SecretKey, async function(err, authData){

		if(err){
			console.log('user onot authorized');
			res.status(401);
		}
		else{
			var data=req.body;
			var uid= authData.user.id;
			var bid= data.bid;
			var pid= data.pid;
			mysqlConnection.query('insert into bookmarks(bid,pid,uid) values (?, ? ,?)',[bid,pid,uid], async function(err,rows){
				if(err){
					var obj ={
						error: true,
						message: 'error_in_bookmark'
					}
					console.log('error in bookmark: ', err);
					res.status(500).send(obj);
				}
				else{
					var obj={
						error: false,
						message :'successfully_bookmarked'
					}
					console.log('bookmark successful');
					res.status(200).send(obj);
				}

			});
		}

	});

});

//remove a bookmark
app.post('/user/post/unbookmark', verifyHeader,verifyToken, upload.none(), async(req,res)=>{

	jwt.verify(req.token, SecretKey, async function(err, authData){

		if(err){
			console.log('user not verified');
			res.status(401);
		}
		else{
			var data= req.body;
			var pid= data.pid;
			var uid= authData.user.id;

			mysqlConnection.query('delete from bookmarks where pid=? AND uid =? ', [pid,uid],async function(err,rows){
				if(err){
					var obj={
						error: true,
						message: 'unable to remove the bookmark'
					};
					res.status(500).send(obj);
				}
				else{
					console.log('bookmark remove successful');
					var obj={
						error: false,
						message: 'unbookmarked_successfully'
					};
					res.status(200).send(obj);
				}
			} )

		}

	});

});


///-----------------------------------------------------------------------------------///
// ------------------------------FUNCTIONS--------------------------------------------//
///----------------------------------------------------------------------------------///

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


//upload post image function
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

//upload profile image function
async function uploadThumbImage(uid, thumb_image_url,token){

	return new Promise(function(resolve,rejct){

		mysqlConnection.query('select thumb_image from user where uid= ? ', [uid],function(err,rows){
			if(err){
				console.log('error in query');
				var obj = {
					error: true,
					message: "Error"
				}
				reject(obj);
			}
			else{
				try{
					var url = rows[0].thumb_image;
					if(url === "" | url === null){
						//image is not present
						mysqlConnection.query('update user set thumb_image=? where uid=?',[thumb_image_url,uid], async function(err,rows){
							if(!err){
								imageUrl= baseURL + "/user/thumbimage/" + token + "/" + thumb_image_url;
								var obj = {
									error: false,
									message: "Success",
									thumb_image:imageUrl
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
						})
				
					}
					else{
						deleteOldProfile(url);
						mysqlConnection.query('update user set thumb_image=? where uid=?',[thumb_image_url,uid], async function(err,rows){
							if(!err){
								imageUrl= baseURL + "/user/thumbimage/" + token + "/" + thumb_image_url;
								var obj = {
									error: false,
									message: "Success",
									thumb_image:imageUrl
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
						})
				
					}
				}
				catch (exception) {
                            console.log('exception', exception);
                   
                        }

			}


		});

		

	});

}

//delete old profile pic
async function deleteOldProfile(_fileName) {
    fileSys.unlink('./public/profile_images/' + _fileName, function (err) {
        if (err) {
            console.log(err);
            throw err;
        }
    });
}

//get user profile (not current) 
async function getUserProfile(passed_user_id, message,type,token){
	

	return new Promise(function(resolve,reject){

		mysqlConnection.query('select post_image,pid from post where uid=?;',[passed_user_id],(err,rows)=>{

			if(err){
				console.log('error in query');
				res.sendStatus(401);

			}
			else{
			//
			console.log('tokenn: ',token);
			posts=[];
			var following_count;
			var followers_count;
			var bio;
			Object.keys(rows).forEach(key => {
				var singlePost=rows[key]
				if (rows[key].post_image != null) {
					singlePost.post_image = baseURL + "/post/image/" + token + "/" + rows[key].post_image;
					console.log('post image:   ', rows[key].post_image);
				}
				posts.push(singlePost);
			
			});
			
		
		//get following count
		mysqlConnection.query('select COUNT(following_uid) as following_count from follow WHERE follower_uid=?',[passed_user_id],(err, rows)=>{
			if(err){
				console.log('err in query 2');
				var obj = {
					error: true,
					message: "Error: " + err
				}
				reject(obj);
			}
			else{
				following_count= rows[0].following_count;
				console.log('following count', following_count);
			}

		});

		//get user bio
		mysqlConnection.query('select bio from user where uid=?', [passed_user_id], (err, rows)=>{
			if(err){
				console.log('err in query 2');
				var obj = {
					error: true,
					message: "Error: " + err
				}
				reject(obj);
			}
			else{
				bio=rows[0].bio;

			}
		})

		//get followers count
		mysqlConnection.query('select COUNT(following_uid) as followers_count from follow WHERE following_uid=?',[passed_user_id],(err, rows)=>{
			if(err){
				console.log('err in query 2');
				var obj = {
					error: true,
					message: "Error: " + err
				}
				reject(obj);
			
			}
			else{
				followers_count= rows[0].followers_count;
				console.log('following count', followers_count);
				
		var obj= {
			error: false,
			message: message,
			type: type,
			following_count: following_count,
			followers_count:followers_count,
			bio:bio,
			post: posts
		}
		resolve(obj);
			}

		});


			
			}
		});


		
	});


}