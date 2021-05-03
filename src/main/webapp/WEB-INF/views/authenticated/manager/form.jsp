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

<acme:form>
	<acme:form-textbox code="authenticated.manager.form.label.team" path="team"/>
	
	<acme:form-submit test="${command == 'create'}" code="authenticated.manager.form.button.create" action="/authenticated/manager/create"/>
	<acme:form-submit test="${command == 'update'}" code="authenticated.manager.form.button.update" action="/authenticated/manager/update"/>
	<acme:form-return code="authenticated.manager.form.button.return"/>
</acme:form>
