<%--
- list.jsp
-
- Copyright (C) 2012-2021 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list readonly="true">
	<acme:list-column code="anonymous.shout.list.label.moment" path="moment" width="20%" sortable="false"/>
	<acme:list-column code="anonymous.shout.list.label.author" path="author" width="20%" sortable="false"/>
	<acme:list-column code="anonymous.shout.list.label.text" path="text" width="30%" sortable="false"/>
	<acme:list-column code="anonymous.shout.list.label.info" path="info" width="30%" sortable="false"/>
	<acme:list-column code="anonymous.shout.label.insignia" path="insignia" width="20%" sortable="false"/>
	<acme:list-column code="anonymous.shout.label.budget" path="budget" width="30%" sortable="false"/>
	<acme:list-column code="anonymous.shout.label.important" path="important" width="30%" sortable="false"/>
	<acme:list-column code="anonymous.shout.label.deadline" path="deadline" width="20%" sortable="false"/>

	

</acme:list>


