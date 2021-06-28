<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<h2>
	<acme:message code="administrator.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<caption>
		<acme:message code="administrator.dashboard.form.title.general-indicators"/>
	</caption>

	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.IoweFlaggedAsImportant"/>
		</th>
		<td>
			<acme:print value="${IoweFlaggedAsImportant}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.IoweRate"/>
		</th>
		<td>
			<acme:print value="${IoweRate}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.IoweEURAvg"/>
		</th>
		<td>
			<acme:print value="${IoweEURAvg}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.IoweEURStd"/>
		</th>
		<td>
			<acme:print value="${IoweEURStd}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.IoweUSDAvg"/>
		</th>
		<td>
			<acme:print value="${IoweUSDAvg}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.IoweUSDStd"/>
		</th>
		<td>
			<acme:print value="${IoweUSDStd}"/>
		</td>
	</tr>

	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfPublicTasks"/>
		</th>
		<td>
			<acme:print value="${numberOfPublicTasks}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfPrivateTasks"/>
		</th>
		<td>
			<acme:print value="${numberOfPrivateTasks}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfFinishedTasks"/>
		</th>
		<td>
			<acme:print value="${numberOfFinishedTasks}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfNonFinishedTasks"/>
		</th>
		<td>
			<acme:print value="${numberOfNonFinishedTasks}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.averageOfTaskExecutionPeriods"/>
		</th>
		<td>
			<acme:print value="${averageOfTaskExecutionPeriods}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.deviationOfTaskExecutionPeriods"/>
		</th>
		<td>
			<acme:print value="${deviationOfTaskExecutionPeriods}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.minOfTaskExecutionPeriods"/>
		</th>
		<td>
			<acme:print value="${minOfTaskExecutionPeriods}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.maxOfTaskExecutionPeriods"/>
		</th>
		<td>
			<acme:print value="${maxOfTaskExecutionPeriods}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.averageOfTaskWorkloads"/>
		</th>
		<td>
			<acme:print value="${averageOfTaskWorkloads}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.deviationOfTaskWorkloads"/>
		</th>
		<td>
			<acme:print value="${deviationOfTaskWorkloads}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.minOfTaskWorkloads"/>
		</th>
		<td>
			<acme:print value="${minOfTaskWorkloads}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.maxOfTaskWorkloads"/>
		</th>
		<td>
			<acme:print value="${maxOfTaskWorkloads}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfWorkPlans"/>
		</th>
		<td>
			<acme:print value="${numberOfWorkPlans}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfPublicWorkPlans"/>
		</th>
		<td>
			<acme:print value="${numberOfPublicWorkPlans}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfPrivateWorkPlans"/>
		</th>
		<td>
			<acme:print value="${numberOfPrivateWorkPlans}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfFinishedWorkPlans"/>
		</th>
		<td>
			<acme:print value="${numberOfFinishedWorkPlans}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.numberOfNonFinishedWorkPlans"/>
		</th>
		<td>
			<acme:print value="${numberOfNonFinishedWorkPlans}"/>
		</td>
	</tr>

	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.averageOfWorkPlanExecutionPeriods"/>
		</th>
		<td>
			<acme:print value="${averageOfWorkPlanExecutionPeriods}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.deviationOfWorkPlanExecutionPeriods"/>
		</th>
		<td>
			<acme:print value="${deviationOfWorkPlanExecutionPeriods}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.minOfWorkPlanExecutionPeriods"/>
		</th>
		<td>
			<acme:print value="${minOfWorkPlanExecutionPeriods}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.maxOfWorkPlanExecutionPeriods"/>
		</th>
		<td>
			<acme:print value="${maxOfWorkPlanExecutionPeriods}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.averageOfWorkplanWorkloads"/>
		</th>
		<td>
			<acme:print value="${averageOfWorkplanWorkloads}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.deviationOfWorkplanWorkloads"/>
		</th>
		<td>
			<acme:print value="${deviationOfWorkplanWorkloads}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.minOfWorkplanWorkloads"/>
		</th>
		<td>
			<acme:print value="${minOfWorkplanWorkloads}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.dashboard.form.label.maxOfWorkplanWorkloads"/>
		</th>
		<td>
			<acme:print value="${maxOfWorkplanWorkloads}"/>
		</td>
	</tr>	
										
</table>
<h2>
	<acme:message code="administrator.dashboard.form.title.chart-workplans"/>
</h2>

<div class="col-6">
	<canvas id="canvas"></canvas>
</div> 
<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"PUBLISHED", "NOT PUBLISHED"
			],
			datasets : [
				{
					data : [
<%--						<jstl:out value="${numberOfWorkPlans}"/>, --%>
						<jstl:out value="${numberOfPublicWorkPlans}"/>, 
						<jstl:out value="${numberOfPrivateWorkPlans}"/>
					],
					backgroundColor: ["blue", "red"],
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0.0,
							suggestedMax : 1.0
						}
					}
				]
			},
			legend : {
				display : false
			}
		};
	
		var canvas, context;
	
		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type : "pie",
			data : data,
			options : options
		});
	});
</script>
