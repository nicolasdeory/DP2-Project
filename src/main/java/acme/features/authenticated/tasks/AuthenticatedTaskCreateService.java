package acme.features.authenticated.tasks;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.ExecutionPeriod;
import acme.entities.tasks.Task;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractCreateService;

@Service
public class AuthenticatedTaskCreateService implements AbstractCreateService<Authenticated, Task>{

	@Autowired
	protected AuthenticatedTaskRepository repository;
	
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
                errors.add("startDateTime","authenticated.tasks.error.startDateTime.format");
            }
        }
        if(request.getModel().hasAttribute("finishDateTime")){
            try{
                executionPeriod.setFinishDateTime(request.getModel().getAttribute("finishDateTime",Date.class));
            }catch(final Exception e){
                errors.add("finishDateTime","authenticated.tasks.error.finishDate.format");
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
//		final List<WorkPlan> userWorkPlans = this.repository.findWorkPlans().stream().collect(Collectors.toList());
//		model.setAttribute("userWorkplans", userWorkPlans);
		
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
            if(!errors.hasErrors("startDateTime")&&entity.getExecutionPeriod().getStartDateTime().before(now) ){
                errors.add("startDateTime", "authenticated.workplan.error.startDate");
            }
            if(entity.getExecutionPeriod().getFinishDateTime().before(now)){
                errors.add("finishDateTime", "authenticated.workplan.error.finishDate");
            }
        }else{
            if(entity.getExecutionPeriod().getStartDateTime()==null){
                errors.add("startDateTime", "authenticated.workplan.error.startDate.empty");
            }
            if(entity.getExecutionPeriod().getFinishDateTime()==null){
                errors.add("finishDateTime", "authenticated.workplan.error.finishDate.empty");
            }
            
	}
        
        //queda validar el workplan
        
	}

	@Override
	public void create(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;
		
//		final UserAccount user = this.repository.findUserById(request.getPrincipal().getAccountId());
//		entity.setUser(user);
//		entity.setWorkPlans(new ArrayList<>());
		
		
		this.repository.save(entity);
		
	}
}
