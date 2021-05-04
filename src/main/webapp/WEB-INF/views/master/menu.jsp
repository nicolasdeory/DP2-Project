<%--
- menu.jsp
-
- Copyright (c) 2012-2021 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java" import="acme.framework.helpers.PrincipalHelper"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.list-shouts" action="/anonymous/shout/list"/>
			<acme:menu-suboption code="master.menu.anonymous.create-shout" action="/anonymous/shout/create"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.anonymous.list-publicWorkplans" action="/anonymous/work-plan/list"/>
			<acme:menu-suboption code="master.menu.anonymous-publicTasks" action="/anonymous/task/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.authenticated-publicTasks" action="/authenticated/task/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.dashboard" action="/administrator/dashboard/show"/>
			<acme:menu-suboption code="master.menu.administrator.spam-parameters" action="/administrator/spam-parameters/show"/>
			<acme:menu-suboption code="master.menu.administrator.user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-initial" action="/master/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/master/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shutdown" action="/master/shutdown"/>
		</acme:menu-option>
	
		<acme:menu-option code="master.menu.management" access="hasRole('Management')">
			<acme:menu-suboption code="master.menu.management.list-Workplans" action="/management/work-plan/list"/>
			<acme:menu-suboption code="master.menu.management.create-Workplans" action="/management/work-plan/create"/>
            <acme:menu-separator/>
            <acme:menu-suboption code="master.menu.management.list-ownTasks" action="/management/task/list"/>
			<acme:menu-suboption code="master.menu.management.create" action="/management/task/create"/>
        </acme:menu-option>		
	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/master/sign-in" access="isAnonymous()"/>

		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-data" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-management" action="/authenticated/management/create" access="!hasRole('Management')"/>
			<acme:menu-suboption code="master.menu.user-account.management" action="/authenticated/management/update" access="hasRole('Management')"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.sign-out" action="/master/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>

