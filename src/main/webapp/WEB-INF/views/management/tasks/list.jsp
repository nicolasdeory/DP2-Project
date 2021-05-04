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

<h1><acme:message code="management.taks.ownList"/></h1>
<acme:list>
	<acme:list-column code="management.tasks.list.label.title" path="title" width="15%" sortable="false"/>
	<acme:list-column code="management.tasks.list.label.description" path="description" width="15%" sortable="false"/>
	<acme:list-column code="management.tasks.list.label.start-date-time" path="startDateTime" width="15%"/>
	<acme:list-column code="management.tasks.list.label.finish-date-time" path="finishDateTime" width="15%"/>
	<acme:list-column code="management.tasks.list.label.workload" path="workload" width="15%"/>
	<acme:list-column code="management.tasks.label.link" path="link" width="15%" sortable="false"/>
</acme:list>
