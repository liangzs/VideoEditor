precision mediump float; 
uniform sampler2D u_Texture2;
uniform sampler2D u_Texture3;
uniform sampler2D u_Texture4;
varying float v_Paticle;
varying vec3 v_Color;
varying float v_ElapsedTime;     	 							    	   								
void main()                    		
{
    float x=gl_PointCoord.x-0.5;
    float y=gl_PointCoord.y-0.5;
    float nx=(cos(v_ElapsedTime)*x - sin(v_ElapsedTime )* y);
    float ny = (sin( v_ElapsedTime )*x + cos( v_ElapsedTime )*y);
   if(v_Paticle==0.0){
       gl_FragColor=texture2D(u_Texture2, vec2( nx+0.5,ny+0.5));
    }else if(v_Paticle==1.0){
        gl_FragColor=texture2D(u_Texture3, vec2( nx+0.5,ny+0.5));
    }else if(v_Paticle==2.0){
        gl_FragColor=texture2D(u_Texture4, vec2( nx+0.5,ny+0.5));
    }
}
