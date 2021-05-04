<%--
- form.jsp
-
- Copyright (c) 2012-2021 Rafael Corchuelo.
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<acme:form>
	<acme:form-textbox code="manager.tasks.label.title" path="title"/>
	<acme:form-textbox code="manager.tasks.label.description" path="description"/>
	<acme:form-checkbox code="manager.tasks.label.is-public" path="isPublic"/>
	
	<jstl:if test="${command != 'create'}">
		<acme:form-textbox code="manager.tasks.label.WorkloadHours" path="workload" readonly ="true"/>
	</jstl:if>
	
	<acme:form-moment code="manager.tasks.label.start-date-time" path="startDateTime" />
	<acme:form-moment code="manager.tasks.label.finish-date-time" path="finishDateTime"/>
	
    
    <jstl:if test="${command == 'show' && isFinished == 'true'}">
    	<acme:message code="manager.tasks.label.FinishedTaskMessage"/>
    </jstl:if>
    <acme:form-submit test="${command == 'show' && isFinished == 'false'}" code="manager.tasks.button.update"
                      action="/manager/task/update"/>
    <acme:form-submit test="${command == 'show' && isFinished == 'false'}" code="manager.tasks.button.delete"
                      action="/manager/task/delete"/>
    <acme:form-submit test="${command == 'create'}" code="manager.tasks.button.create"
                      action="/manager/task/create"/>
    <acme:form-submit test="${command == 'update'}" code="manager.tasks.button.update"
                      action="/manager/task/update"/>
    <acme:form-submit test="${command == 'delete'||command == 'update'}" code="manager.tasks.button.delete"
                      action="/manager/task/delete"/>
    <acme:form-return code="manager.tasks.button.return"/>
</acme:form>
