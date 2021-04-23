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
	<acme:form-textbox code="authenticated.workplan.label.title" path="title"/>
	<acme:form-textbox code="authenticated.workplan.label.description" path="description"/>
	<acme:form-checkbox code="authenticated.workplan.label.description" path="isPublic"/>
	<jstl:if test="${command != 'create'}">
		<acme:form-textbox code="authenticated.workplan.label.WorkloadHours" path="workload" readonly="true"/>
	</jstl:if>

	<acme:form-moment code="authenticated.workplan.label.start-date-time" path="startDateTime" />
	<acme:form-moment code="authenticated.workplan.label.finish-date-time" path="finishDateTime"/>
	<div class="form-group">
		<label for="tasks">
			<acme:message code="authenticated.workplan.label.task" />
		</label>
		<select id="tasks" size="6" class="form-control">
			<c:forEach items="${tasks}" var="task">
				<option><c:out value="${task.title}"/></option>
			</c:forEach>
		</select>
	</div>
	<acme:form-submit test="${command == 'show' && isFinished == 'false'}" code="authenticated.workplan.button.update" action="/authenticated/work-plan/update"/>
	<acme:form-submit test="${command == 'show' && isFinished == 'false'}" code="authenticated.workplan.button.delete" action="/authenticated/work-plan/delete"/>
	<acme:form-submit test="${command == 'create'}" code="authenticated.workplan.button.create" action="/authenticated/work-plan/create"/>
	<acme:form-submit test="${command == 'update'}" code="authenticated.workplan.button.update" action="/authenticated/work-plan/update"/>
	<acme:form-submit test="${command == 'delete'}" code="authenticated.workplan.button.delete" action="/authenticated/work-plan/delete"/>
	<acme:form-return code="authenticated.workplan.button.return"/>
</acme:form>
