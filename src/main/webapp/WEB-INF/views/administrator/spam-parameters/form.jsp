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
<%@ taglib prefix="jslt" uri="http://java.sun.com/jsp/jstl/core" %>

<acme:form>
	<div class="col-6">
		<acme:form-textbox code="administrator.spam.form.label.threshold" path="threshold" />
		<acme:form-textbox code="administrator.spam.form.label.addKeyword" path="newKeyword" />
		<button class="btn btn-primary mb-4" id="add-keyword"><acme:message code="administrator.spam.form.button.addKeyword" /></button>
        <input type='hidden' name='keywords' />
		<div class="form-group">
			<label for="keywords">
				<acme:message code="administrator.spam.form.label.keywords" />
			</label>
			<select id="keywords" size="6" class="form-control">
				<jslt:forEach items="${keywords}" var="kw">
					<option value="${kw}">${kw}</option>
				</jslt:forEach>
			</select>
		</div>
		<button class="btn btn-primary mb-4" id="remove-keyword"><acme:message code="administrator.spam.form.button.removeKeyword" /></button>
		<br>
		<acme:form-submit code="administrator.spam.form.button.update" action="/administrator/spam-parameters/update"/>
		<acme:form-return code="administrator.user-account.form.button.return"/>
	</div>
</acme:form>
<script src="spam.js"></script>