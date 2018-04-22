package io.crnk.setup.vertx.suite;

import io.crnk.client.CrnkClient;
import io.crnk.core.repository.RelationshipRepositoryV2;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.utils.Supplier;
import io.crnk.setup.vertx.CrnkVehicle;
import io.crnk.test.mock.ClientTestModule;
import io.crnk.test.mock.models.RelationIdTestResource;
import io.crnk.test.mock.models.Schedule;
import io.crnk.test.suite.TestContainer;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.Serializable;

public class VertxTestContainer implements TestContainer {

	private CrnkVehicle vehicle;

	private Supplier<CrnkClient> client;

	private VertxOptions options = new VertxOptions();

	private Vertx vertx;

	public VertxTestContainer() {
		client = () -> {
			CrnkClient client = new CrnkClient(this.getBaseUrl());
			client.addModule(new ClientTestModule());
			return client;
		};
		options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
	}

	@Override
	public void start() {
		vehicle = new CrnkVehicle();
		vertx = Vertx.vertx(options);
		vertx.deployVerticle(vehicle);
		vehicle.testModule.clear();
	}

	@Override
	public void stop() {
		vertx.close();
		vehicle.testModule.clear();
	}

	@Override
	public <T, I extends Serializable> ResourceRepositoryV2<T, I> getRepositoryForType(Class<T> resourceClass) {
		return client.get().getRepositoryForType(resourceClass);
	}

	@Override
	public <T, I extends Serializable, D, J extends Serializable> RelationshipRepositoryV2<T, I, D, J> getRepositoryForType(Class<T> sourceClass, Class<D> targetClass) {
		return client.get().getRepositoryForType(sourceClass, targetClass);
	}

	@Override
	public <T> T getTestData(Class<T> clazz, Object id) {
		if (clazz == Schedule.class) {
			return (T) vehicle.testModule.getScheduleRepository().getMap().get((Long) id);
		}
		if (clazz == RelationIdTestResource.class) {
			return (T) vehicle.testModule.getRelationIdTestRepository().getMap().get((Long) id);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public String getBaseUrl() {
		return "http://127.0.0.1:" + vehicle.port;
	}
}
