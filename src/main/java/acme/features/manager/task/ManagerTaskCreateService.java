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
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractCreateService;

@Service
public class ManagerTaskCreateService implements AbstractCreateService<Manager, Task>{

	@Autowired
	protected ManagerTaskRepository repository;
	
	@Override
	public boolean authorise(final Request<Task> request) {
		assert request != null;

            return true;
        
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
                errors.state(request, false,"startDateTime","manager.tasks.error.startDateTime.format");
            }
        }
        if(request.getModel().hasAttribute("finishDateTime")){
            try{
                executionPeriod.setFinishDateTime(request.getModel().getAttribute("finishDateTime",Date.class));
            }catch(final Exception e){
                errors.state(request, false,"finishDateTime","manager.tasks.error.finishDate.format");
            }

        }
        entity.setExecutionPeriod(executionPeriod);
		
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		//revisar
		
		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime"); 
		request.unbind(entity, model, "title","isPublic", "description", "link");
		
	}

	@Override
	public Task instantiate(final Request<Task> request) {
		assert request != null;

		Task task;
		task = new Task();
		task.setExecutionPeriod(new ExecutionPeriod());
		
		return task;
	}

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;	
		
		final Date now=new Date(System.currentTimeMillis());
		
        if(entity.getExecutionPeriod().getStartDateTime()!=null&&entity.getExecutionPeriod().getFinishDateTime()!=null){
            if(entity.getExecutionPeriod().getStartDateTime().before(now) ){
                errors.state(request, false,"startDateTime", "manager.tasks.error.startDate");
            }
            if(entity.getExecutionPeriod().getFinishDateTime().before(now)){
                errors.state(request, false,"finishDateTime", "manager.tasks.error.finishDate");
            }
            if(entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())){
                errors.state(request, false,"startDateTime","manager.tasks.error.startDate.after");
                errors.state(request, false,"finishDateTime","manager.tasks.error.finishDate.before");
            }
        }else{
            if(entity.getExecutionPeriod().getStartDateTime()==null){
                errors.state(request, false,"startDateTime", "manager.tasks.error.startDate.empty");
            }
            if(entity.getExecutionPeriod().getFinishDateTime()==null){
                errors.state(request, false,"finishDateTime", "manager.tasks.error.finishDate.empty");
            }
            
	}
        
        
	}

	@Override
	public void create(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;
		
		final UserAccount user = this.repository.findUserById(request.getPrincipal().getAccountId());
		entity.setUser(user);
		
		
		this.repository.save(entity);
		
	}
}
