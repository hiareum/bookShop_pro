<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	isELIgnored="false"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
</head>
<BODY>
	<H1>결제실패</H1>

<form  name="form_order">

	<br>
	<br>
	<br>
	<H1>결제정보</H1>
	<DIV class="detail_table">
		<table>
			<TBODY>
				<TR class="dot_line">
					<TD class="fixed_join" style="background-color:#FFE400; color: black;">결제방법</TD>
					<TD>
					   ${returnMap.type }
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join" style="background-color:#FFE400; color: black;">결제코드</TD>
					<TD>
					   ${returnMap.responseCode }
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join" style="background-color:#FFE400; color: black;">결제메세지</TD>
					<TD>
					   ${returnMap.responseMsg }
				    </TD>
				</TR>
				
				
			</TBODY>
		</table>
	</DIV>
	
	
</form>
    <DIV class="clear"></DIV>
	<br>
	<br>
	<br>
	<center>
		<br>
		<br> 
		<a href="${contextPath}/main/main.do"> 
		   <IMG width="75" alt="" src="${contextPath}/resources/image/btn_shoping_continue.jpg">
		</a>
<DIV class="clear"></DIV>		
	
			
			
			