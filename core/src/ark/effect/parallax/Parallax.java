package ark.effect.parallax;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class Parallax extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Decal decal;
	DecalBatch decalBatch;
	PerspectiveCamera camera;
	CameraInputController cameraInputController;

	float prev_y;
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera=new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.position.set(0,-10,0);
		camera.lookAt(0,0,0);
		camera.near =0.1f;
		camera.far = 200f;

		cameraInputController=new CameraInputController(camera);
		Gdx.input.setInputProcessor(cameraInputController);
		img = new Texture("badlogic.jpg");
		decal=Decal.newDecal(10,10,new TextureRegion(img));
		decal.setPosition(0,0,0);

		CameraGroupStrategy cameraGroupStrategy = new CameraGroupStrategy(camera);;
		decalBatch=new DecalBatch(cameraGroupStrategy);
		decalBatch.add(decal);

		prev_y=Gdx.input.getAccelerometerY();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.4f, 1, 0);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

		batch.begin();
		camera.update();

		Gdx.app.log("pitch",String.valueOf(Gdx.input.getPitch()));
		Gdx.app.log("roll",String.valueOf(Gdx.input.getRoll()));

		/*decal.rotateX(1);
		decal.rotateY(1);
		decal.rotateZ(1);*/
		decal.rotateY((prev_y-Gdx.input.getAccelerometerY())*4f);
		prev_y=Gdx.input.getAccelerometerY();

		decalBatch.add(decal);
		decalBatch.flush();

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
