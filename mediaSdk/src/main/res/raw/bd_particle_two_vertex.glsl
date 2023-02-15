uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;  
attribute vec3 a_Color;
attribute float a_Paticle;
attribute vec3 a_DirectionVector;
attribute float a_ParticleStartTime;
attribute float a_PointSize;
varying vec3 v_Color;
varying float v_ElapsedTime;
varying float v_Paticle;

void main()                    
{                                	  	  
    v_Color = a_Color;
    v_Paticle=a_Paticle;
    v_ElapsedTime = u_Time - a_ParticleStartTime;
    float gravityFactor = v_ElapsedTime;
    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);
    gl_Position = u_Matrix * vec4(currentPosition, 1.0);
    gl_PointSize = a_PointSize;
}
