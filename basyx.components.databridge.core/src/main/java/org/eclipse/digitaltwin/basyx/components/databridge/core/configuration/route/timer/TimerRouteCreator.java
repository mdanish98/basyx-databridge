package org.eclipse.digitaltwin.basyx.components.databridge.core.configuration.route.timer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.eclipse.digitaltwin.basyx.components.databridge.core.configuration.route.core.AbstractRouteCreator;
import org.eclipse.digitaltwin.basyx.components.databridge.core.configuration.route.core.RouteConfiguration;
import org.eclipse.digitaltwin.basyx.components.databridge.core.configuration.route.core.RouteCreatorHelper;
import org.eclipse.digitaltwin.basyx.components.databridge.core.configuration.route.core.RoutesConfiguration;

public class TimerRouteCreator extends AbstractRouteCreator {
	private static final Long TIMEOUT = 5000L;

	public TimerRouteCreator(RouteBuilder routeBuilder, RoutesConfiguration routesConfiguration) {
		super(routeBuilder, routesConfiguration);
	}

	@Override
	protected void configureRoute(RouteConfiguration routeConfig, String dataSourceEndpoint, String[] dataSinkEndpoints, String[] dataTransformerEndpoints, String routeId) {
		TimerRouteConfiguration timerConfig = (TimerRouteConfiguration) routeConfig;
		String timerEndpoint = RouteCreatorHelper.getDataSourceEndpoint(getRoutesConfiguration(), timerConfig.getTimerName());
		RouteDefinition routeDefinition = getRouteBuilder().from(timerEndpoint).pollEnrich(dataSourceEndpoint, TIMEOUT).routeId(routeId).to("log:" + routeId);

		if (!(dataTransformerEndpoints == null || dataTransformerEndpoints.length == 0)) {
			routeDefinition.to(dataTransformerEndpoints).to("log:" + routeId);
		}

		routeDefinition.to(dataSinkEndpoints[0]).to("log:" + routeId);
	}

}