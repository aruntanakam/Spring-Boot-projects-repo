<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<style>
	div{
		
		padding:5px;
		background-color: yellow;
		color:black;
		font-family: Arial, Helvetica, sans-serif;
		font-size: 15px;
	}
	
	
</style>

</head>
<body>
   
   <div>
    <pre>
Hello <b>${userName}</b>,

               Please see the attached file in the mail to check your monthly expenses for the month of <b>${month}</b> Year.
               If it asks for password then please use your Date of Birth in the format of DDMMYYYY(for example  if your DOB 
               is july 1 2000 then password is 01072000.

Thanks and Regards
Application Team
mail:${fromAddress}

	</pre>
	
	<center><img src="cid:expenseImg" width="200" height="200"  alt="expense image"/></center>
	
	</div>
</body>
</html>