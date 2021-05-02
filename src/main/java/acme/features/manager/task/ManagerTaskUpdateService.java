package acme.features.manager.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.ExecutionPeriod;
import acme.entities.roles.Manager;
import acme.entities.tasks.Task;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractUpdateService;

@Service
public class ManagerTaskUpdateService implements AbstractUpdateService<Manager, Task>{

	@Autowired
	protected ManagerTaskRepository repository;
	
	@Override
	public boolean authorise(final Request<Task> request) {
		assert request != null;

        int taskId;
        final Task task;
        UserAccount userAccount;
        Principal principal;

        taskId = request.getModel().getInteger("id");
        task = this.repository.findOneTaskById(taskId);
        userAccount = task.getUser();
        principal = request.getPrincipal();
        if (userAccount.getId() == principal.getAccountId()) {
            return true;
        } else {
            return false;
        }
	}

	@Override
	public void bind(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		final ExecutionPeriod executionPeriod = new ExecutionPeriod();
        request.bind(entity, errors);
        if(request.getModel().hasAttribute("startDateTime")){
            try{
                executionPeriod.setStartDateTime(request.getModel().getAttribute("startDateTime",Date.class));
            }
            catch(final Exception e){
                errors.state(request, false,"startDateTime","authenticated.tasks.error.startDateTime.format");
            }
        }
        if(request.getModel().hasAttribute("finishDateTime")){
            try{
                executionPeriod.setFinishDateTime(request.getModel().getAttribute("finishDateTime",Date.class));
            }catch(final Exception e){
                errors.state(request, false, "finishDateTime","authenticated.tasks.error.finishDate.format");
            }

        }
        
        entity.setExecutionPeriod(executionPeriod);
		
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime"); 
		request.unbind(entity, model, "title","isPublic", "description", "link");
		
	}

	@Override
	public Task findOne(final Request<Task> request) {
		assert request != null;

		Task task;
		int id;
		
		id = request.getModel().getInteger("id");
		task = this.repository.findOneTaskById(id);
		
		return task;
	}

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;	
		
		final Date now=new Date(System.currentTimeMillis());
        if(entity.getExecutionPeriod().getStartDateTime()!=null&&entity.getExecutionPeriod().getFinishDateTime()!=null){
            if(!errors.hasErrors("startDateTime")&&entity.getExecutionPeriod().getStartDateTime().before(now) ){
                errors.state(request, false, "startDateTime", "manager.tasks.error.startDate");
            }
            if(entity.getExecutionPeriod().getFinishDateTime().before(now)){
                errors.state(request, false, "finishDateTime", "manager.tasks.error.finishDate");
            }
            if(entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())){
                errors.state(request, false, "startDateTime","manager.tasks.error.startDate.after");
                errors.state(request, false, "finishDateTime","manager.tasks.error.finishDate.before");
            }
        }else{
            if(entity.getExecutionPeriod().getStartDateTime()==null){
                errors.state(request, false, "startDateTime", "manager.tasks.error.startDate.empty");
            }
            if(entity.getExecutionPeriod().getFinishDateTime()==null){
            	 errors.state(request, false, "finishDateTime", "manager.tasks.error.finishDate.empty");
            }

        }

        if(errors.hasErrors()){
            this.unbind(request,entity,request.getModel());
        }
		
	}

	@Override
	public void update(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;
		
		this.repository.save(entity);
		
	}

}