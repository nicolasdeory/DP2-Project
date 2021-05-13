package acme.features.management.task;

import java.util.Date;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.ExecutionPeriod;
import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractUpdateService;

@Service
public class ManagementTaskUpdateService implements AbstractUpdateService<Management, Task>{

    private static final String START_DATE_TIME = "startDateTime";
    private static final String FINISH_DATE_TIME = "finishDateTime";

	@Autowired
	protected ManagementTaskRepository repository;
	
	@Override
	public boolean authorise(final Request<Task> request) {
		AssertUtils.assertRequestNotNull(request);

        int taskId;
        final Task task;
        UserAccount userAccount;
        Principal principal;

        taskId = request.getModel().getInteger("id");
        task = this.repository.findOneTaskById(taskId);
        userAccount = task.getUser();
        principal = request.getPrincipal();
        return userAccount.getId() == principal.getAccountId();
	}

	@Override
	public void bind(final Request<Task> request, final Task entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

		final ExecutionPeriod executionPeriod = new ExecutionPeriod();
        request.bind(entity, errors);
        if(request.getModel().hasAttribute(START_DATE_TIME)){
            try{
                executionPeriod.setStartDateTime(request.getModel().getAttribute(START_DATE_TIME,Date.class));
            }
            catch(final Exception e){
                errors.state(request, false,START_DATE_TIME,"authenticated.tasks.error.startDateTime.format");
            }
        }
        if(request.getModel().hasAttribute(FINISH_DATE_TIME)){
            try{
                executionPeriod.setFinishDateTime(request.getModel().getAttribute(FINISH_DATE_TIME,Date.class));
            }catch(final Exception e){
                errors.state(request, false, FINISH_DATE_TIME,"authenticated.tasks.error.finishDate.format");
            }

        }
        
        entity.setExecutionPeriod(executionPeriod);
		
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);
		
		request.unbind(entity.getExecutionPeriod(), model, START_DATE_TIME, FINISH_DATE_TIME);
		request.unbind(entity, model, "title","isPublic", "description", "link");
		
	}

	@Override
	public Task findOne(final Request<Task> request) {
		AssertUtils.assertRequestNotNull(request);

		Task task;
		int id;
		
		id = request.getModel().getInteger("id");
		task = this.repository.findOneTaskById(id);
		
		return task;
	}

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);
		
		final Date now=new Date(System.currentTimeMillis());
        if(entity.getExecutionPeriod().getStartDateTime()!=null&&entity.getExecutionPeriod().getFinishDateTime()!=null){
            if(entity.getExecutionPeriod().getStartDateTime().before(now) ){
                errors.state(request, false, START_DATE_TIME, "management.tasks.error.startDate");
            }
            if(entity.getExecutionPeriod().getFinishDateTime().before(now)){
                errors.state(request, false, FINISH_DATE_TIME, "management.tasks.error.finishDate");
            }
            if(entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())){
                errors.state(request, false, START_DATE_TIME,"management.tasks.error.startDate.after");
                errors.state(request, false, FINISH_DATE_TIME,"management.tasks.error.finishDate.before");
            }
        }else{
            if(entity.getExecutionPeriod().getStartDateTime()==null){
                errors.state(request, false, START_DATE_TIME, "management.tasks.error.startDate.empty");
            }
            if(entity.getExecutionPeriod().getFinishDateTime()==null){
            	 errors.state(request, false, FINISH_DATE_TIME, "management.tasks.error.finishDate.empty");
            }

        }

        if(errors.hasErrors()){
            this.unbind(request,entity,request.getModel());
        }
		
	}

	@Override
	public void update(final Request<Task> request, final Task entity) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		
		this.repository.save(entity);
		
	}

}
