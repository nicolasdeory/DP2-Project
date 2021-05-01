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
	<acme:form-textbox code="authenticated.tasks.label.title" path="title"/>
	<acme:form-textbox code="authenticated.tasks.label.description" path="description"/>
	<acme:form-checkbox code="authenticated.tasks.label.is-public" path="isPublic"/>
	
	<jstl:if test="${command != 'create'}">
		<acme:form-textbox code="authenticated.tasks.label.WorkloadHours" path="workload" readonly ="true"/>
	</jstl:if>
	<acme:form-moment code="authenticated.tasks.label.start-date-time" path="startDateTime" />
	<acme:form-moment code="authenticated.tasks.label.finish-date-time" path="finishDateTime"/>
	<div class="form-group">
		<label for="workPlans">
			<acme:message code="authenticated.tasks.label.workPlan" />
		</label>
		 <select id="workplans" size="6" class="form-control">
			<c:forEach items="${workPlans}" var="wp">
				<acme:form-option code="" value="${wp.title}"/>
			</c:forEach>
		</select>
	</div>
	
    
    <acme:form-submit code="authenticated.tasks.button.update" action="/authenticated/task/update"/>
    <acme:form-submit code="authenticated.tasks.button.delete" action="/authenticated/task/delete"/>
    <acme:form-return code="authenticated.tasks.button.return"/>
</acme:form>
