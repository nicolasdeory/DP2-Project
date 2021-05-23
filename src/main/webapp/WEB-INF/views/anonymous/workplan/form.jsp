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
	<acme:form-textbox code="anonymous.workplan.label.title" path="title"/>
	<acme:form-textbox code="anonymous.workplan.label.description" path="description"/>
	<acme:form-textbox code="anonymous.workplan.label.WorkloadHours" path="workload"/>
	<acme:form-textbox code="anonymous.workplan.label.start-date-time" path="startDateTime" />
	<acme:form-textbox code="anonymous.workplan.label.finish-date-time" path="finishDateTime"/>
	<label for="tasks">
		<acme:message code="anonymous.workplan.label.task"/>
	</label>
	<div style="max-height: 40vmin; overflow-y: auto">
		<table class="table table-striped" >
			<tr>
				<th>
					<acme:message code="anonymous.workplan.label.title"/>
				</th>
				<th>
					<acme:message code="anonymous.workplan.label.start-date-time"/>
				</th>
				<th>
					<acme:message code="anonymous.workplan.label.finish-date-time"/>
				</th>
			</tr>
			<c:forEach items="${tasks}" var="task">
				<tr>
					<td id="${task.title}">
						<c:out value="${task.title}"/>
					</td>
					<td>
						<c:out value="${task.executionPeriod.startDateTime}"/>
					</td>
					<td>
						<c:out value="${task.executionPeriod.finishDateTime}"/>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<br>
	

    <jstl:if test="${isFinished == 'true'}">
    	<acme:message code="anonymous.workplan.label.FinishedTaskMessage"/>
    </jstl:if>
    
  	<acme:form-return code="anonymous.workplan.button.return"/>
</acme:form>
