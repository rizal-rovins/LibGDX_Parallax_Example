package ark.effect.parallax;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.MathUtils;

public class Parallax extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture body;
	OrthographicCamera camera;
	CameraInputController cameraInputController;

	static double gravityFilterK =0.8;
	double gravityVector[];
	double lastGravity[];
	double roll,pitch;
	double dgX,dgY;

	float w,h;
	BitmapFont font;

	@Override
	public void create () {
		gravityVector=new double[3];
		lastGravity=new double[2];
		w=Gdx.graphics.getWidth()/2;
		h=Gdx.graphics.getHeight()/2;
		batch = new SpriteBatch();
		font=new BitmapFont();
		font.getData().scale(3);
		camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.position.set(0,-10,0);

		cameraInputController=new CameraInputController(camera);
		Gdx.input.setInputProcessor(cameraInputController);
		img = new Texture("logo.png");
		body = new Texture("bdy.jpg");


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.4f, 1, 0);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

		parallaxCalculator();

		if ((dgX != 0) || (dgY != 0))

		{

			w += dgX * (1);

			h -= dgY * (1);

		}


		batch.begin();

		batch.draw(body,Gdx.graphics.getWidth()/2-body.getWidth()/2,Gdx.graphics.getHeight()/2-body.getHeight()/2);
		batch.draw(img,w-img.getWidth()/2,h-img.getHeight()/2);
/*		font.draw(batch,"X-"+String.valueOf(Gdx.input.getGyroscopeX()),30,30);

		font.draw(batch,"Y-"+String.valueOf(Gdx.input.getGyroscopeY()),90,90);

		font.draw(batch,"Z-"+String.valueOf(Gdx.input.getGyroscopeZ()),150,150);
*/
		camera.update();


		batch.end();
	}

	public void parallaxCalculator()
	{
		gravityVector[0] = gravityVector[0] * gravityFilterK + (1- gravityFilterK)* Gdx.input.getAccelerometerX();

		gravityVector[1] = gravityVector[1] * gravityFilterK + (1- gravityFilterK) * Gdx.input.getAccelerometerY();

		gravityVector[2] = gravityVector[2] * gravityFilterK + (1- gravityFilterK) * Gdx.input.getAccelerometerZ();

		double gX=gravityVector[0];
		double gY=gravityVector[1];
		double gZ=gravityVector[2];


		double gSum = Math.sqrt(gX*gX + gY*gY + gZ*gZ);

		if (gSum != 0)

		{

			gX /= gSum;

			gY /= gSum;

			gZ /= gSum;

		}

		if (gZ != 0)

			roll = Math.atan2(gX, gZ) * 180/Math.PI;

		pitch = Math.sqrt(gX*gX + gZ*gZ);

		if (pitch != 0)

			pitch = Math.atan2(gY, pitch) * 180/Math.PI;

		dgX = (roll- lastGravity[0]);

		dgY = (pitch- lastGravity[1]);

// if device orientation is close to vertical – rotation around x is almost undefined – skip!

		if (gY > 0.99) dgX = 0;

// if rotation was too intensive – more than 180 degrees – skip it

		if (dgX > 180) dgX = 0;

		if (dgX < -180) dgX = 0;

		if (dgY > 180) dgY = 0;

		if (dgY < -180) dgY = 0;

		/*if (!screen->isPortrait())

		{

			// Its landscape mode – swap dgX and dgY

			double temp = dgY;

			dgY = dgX;

			dgX = temp;

		}
		*/

		lastGravity[0] = roll;

		lastGravity[1] = pitch;

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
