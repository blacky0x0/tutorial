package my.vaadin.app;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;

public class CustomerForm extends CustomerFormDesign {

	CustomerService service = CustomerService.getInstance();
	private Customer customer;
	private MyUI myUI;

	public CustomerForm(MyUI myUI) {
		this.myUI = myUI;
		status.addItems(CustomerStatus.values());
		save.setClickShortcut(KeyCode.ENTER);
		save.addClickListener(e -> this.save());
		delete.addClickListener(e -> this.delete());
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		BeanFieldGroup.bindFieldsUnbuffered(customer, this);

		// Show delete button for only customers already in the database
		delete.setVisible(customer.isPersisted());
		setVisible(true);
		firstName.selectAll();
	}

	private void delete() {
		// Create a sub-window and set the content
		final Window modal = new Window("Continue?");
		final HorizontalLayout content = new HorizontalLayout();
		final Button ok = new Button("OK");
		final Button cancel = new Button("Cancel");

		ok.addClickListener(event -> {
			service.delete(customer);
			myUI.updateList();
			setVisible(false);

			modal.close();
		});
		cancel.addClickListener(event -> {
			modal.close();
		});

		// Set components
		modal.setContent(content);
		content.addComponent(ok);
		content.addComponent(cancel);
		content.setSizeUndefined();
		content.setMargin(true);
		content.setSpacing(true);

		// Center it in the browser window
		modal.center();
		modal.setVisible(true);
		modal.setModal(true);

		myUI.addWindow(modal);
	}

	private void save() {
		service.save(customer);
		myUI.updateList();
		setVisible(false);
	}
}
