/*
 * WhiteLabelErrorController.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WhiteLabelErrorController implements ErrorController {

	// ErrorController interface ----------------------------------------------

	@RequestMapping("/error")
	public ModelAndView handleError() {
		ModelAndView result;

		result = new ModelAndView("redirect:/master/panic");

		return result;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
