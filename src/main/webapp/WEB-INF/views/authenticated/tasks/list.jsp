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

<h1><acme:message code="authenticated.taks"/></h1>
<acme:list>
	<acme:list-column code="authenticated.tasks.label.title" path="title" width="20%"  sortable="false"/>
	<acme:list-column code="authenticated.tasks.label.start-date-time" path="startDateTime" width="20%"/>
	<acme:list-column code="authenticated.tasks.label.finish-date-time" path="finishDateTime" width="20%"/>
	<acme:list-column code="authenticated.tasks.label.workload" path="workload" width="20%"/>
	<acme:list-column code="authenticated.tasks.label.link" path="link" width="20%" sortable="false"/>
</acme:list>
