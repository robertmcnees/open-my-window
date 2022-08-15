package com.openmywindow.ui;

import java.net.URI;
import java.util.Optional;

import javax.swing.text.html.Option;

import com.openmywindow.ui.record.WindowRecord;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@PageTitle("Open My Window")
@Route
public class MainView extends VerticalLayout {

	@Value("${arbiterserviceurl:omw-arbiter}")
	private String arbiterServiceUrl;

	@Value("${arbiterserviceport:#{null}}")
	private String arbiterServicePort;

	TextField postalCodeTextField = new TextField();
	TextField comfortableHumidityTextField = new TextField();

	Label windowRecommendation = new Label();
	Label tempRecommendation = new Label();
	Label humidityRecommendation = new Label();
	Label nextRecommendation = new Label();
	Label currentTemp = new Label();
	Label currentHumidity = new Label();
	Label dailyHighTemp = new Label();
	Label dailyLowTemp = new Label();

	public MainView() {

		add(new Section(new H1("Open My Window")));

		postalCodeTextField.setLabel("Enter postal code");
		add(postalCodeTextField);

		comfortableHumidityTextField.setLabel("Comfortable Humidity");
		add(comfortableHumidityTextField);

		add(new Button("Open My Window?",
				buttonClickEvent -> handleButtonClick(buttonClickEvent)));

		add(new Section(
				new H2("Recommendation"), windowRecommendation));

		add(new Section(
				new H3("Temperature Recommendation"), tempRecommendation));

		add(new Section(
				new H3("Humidity Recommendation"), humidityRecommendation));

		add(new Section(
				new H3("Next Recommendation"), nextRecommendation));

		add(new Section(
				new H4("Current Temp"), currentTemp));

		add(new Section(
				new H4("Current Humidity"), currentHumidity));

		add(new Section(
				new H4("Daily High Temp"), dailyHighTemp));

		add(new Section(
				new H4("Daily Low Temp"), dailyLowTemp));

	}

	public void handleButtonClick(ClickEvent<Button> buttonClickEvent) {

		Optional<String> comfortableHumidityOptional = (comfortableHumidityTextField == null || comfortableHumidityTextField.getValue().equals("")) ? Optional.empty() : Optional.of(comfortableHumidityTextField.getValue());
		URI omwURI = UriComponentsBuilder.newInstance().scheme("http").host(arbiterServiceUrl).port(arbiterServicePort)
				.path("/arbiter").path("/window")
				.queryParam("postalCode", postalCodeTextField.getValue())
				.queryParamIfPresent("comfortableHumidity", comfortableHumidityOptional)
				.build(true).toUri();

		WebClient.RequestHeadersSpec<?> spec = WebClient.create().
				get().uri(omwURI);
		WindowRecord windowRecord = spec.retrieve().bodyToMono(WindowRecord.class).block();

		windowRecommendation.setText(windowRecord.status());
		tempRecommendation.setText(windowRecord.tempRecommendation());
		humidityRecommendation.setText(windowRecord.humidityRecommendation());
		nextRecommendation.setText(windowRecord.nextRecommendation());
		currentTemp.setText(windowRecord.currentTemp().toString());
		currentHumidity.setText(windowRecord.currentHumidity().toString());
		dailyHighTemp.setText(windowRecord.dailyHighTemp().toString());
		dailyLowTemp.setText(windowRecord.dailyLowTemp().toString());

	}
}
