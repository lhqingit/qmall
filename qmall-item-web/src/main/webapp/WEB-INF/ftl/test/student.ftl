<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>测试FreeMarker</title>
	</head>
	<body>
		学生信息：<br/>
		学号：${student.id}<br/>
		姓名：${student.name}<br/>
		年龄：${student.age}<br/>
		地址：${student.address}<br/>
		学生列表信息：
		<table border="2">
			<tr>
				<th>序号</th>
				<th>学号</th>
				<th>姓名</th>
				<th>年龄</th>
				<th>地址</th>
			</tr>
			<#list stuList as stu>
				<#if stu_index % 4 == 0>
				<tr bgcolor="red">
				<#elseif stu_index % 4 == 1>
				<tr bgcolor="green">
				<#elseif stu_index % 4 == 2>
				<tr bgcolor="blue">
				<#else >
				<tr bgcolor="yellow">
				</#if>
					<td>${stu_index}</td>
					<td>${stu.id}</td>
					<td>${stu.name}</td>
					<td>${stu.age}</td>
					<td>${stu.address}</td>
				</tr>
			</#list>
		</table>
		当前日期：${dateQ?date}<br/>
		当前时间：${dateQ?time}<br/>
		当前日期时间：${dateQ?datetime}<br/>
		自定义格式化时间：${dateQ?string("yyyy/MM/dd HH:mm:ss")}<br/>
		NULL值的处理：${isANullVal!'我是一个NULL值'}
		<#if isANullVal??> 
			（有内容）
		<#else>
			（没内容）
		</#if>
		<br/>
		引入模板测试：<#include "hello.ftl">
		
	</body>
	
</html>