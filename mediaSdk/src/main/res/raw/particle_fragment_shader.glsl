precision mediump float; 
uniform sampler2D u_TextureUnit;
varying vec3 v_Color;
varying float v_ElapsedTime;     	 							    	   								
void main()                    		
{
    /*
    float xDistance = 0.5 - gl_PointCoord.x;
    float yDistance = 0.5 - gl_PointCoord.y;
    float distanceFromCenter =
        sqrt(xDistance * xDistance + yDistance * yDistance);
    gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0);

    if (distanceFromCenter > 0.5) {
        discard;
    } else {
        gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0);
    }
       gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0)
                     * texture2D(u_TextureUnit, gl_PointCoord);
    */
    float x=gl_PointCoord.x-0.5;
    float y=gl_PointCoord.y-0.5;
    float nx=(cos(v_ElapsedTime)*x - sin(v_ElapsedTime )* y);
    float ny = (sin( v_ElapsedTime )*x + cos( v_ElapsedTime )*y);
    //gl_FragColor = vec4(v_Color,1.0)*texture2D(u_TextureUnit, vec2( nx+0.5,ny+0.5));暂时不做颜色改变
    gl_FragColor = texture2D(u_TextureUnit, vec2( nx+0.5,ny+0.5));
}
