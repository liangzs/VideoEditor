precision mediump float; 
uniform sampler2D u_Texture1;
uniform sampler2D u_Texture2;
uniform sampler2D u_Texture3;
uniform sampler2D u_Texture4;
uniform sampler2D u_Texture5;
uniform sampler2D u_Texture6;
uniform sampler2D u_Texture7;
uniform sampler2D u_Texture8;
varying float v_Paticle;
varying float v_Alpha;
void main()
{
   if(v_Paticle==0.0){
       gl_FragColor=texture2D(u_Texture1, gl_PointCoord)*v_Alpha;
    }else if(v_Paticle==1.0){
       gl_FragColor=texture2D(u_Texture2, gl_PointCoord)*v_Alpha;
    }else if(v_Paticle==2.0){
       gl_FragColor=texture2D(u_Texture3, gl_PointCoord)*v_Alpha;
    }else if(v_Paticle==3.0){
       gl_FragColor=texture2D(u_Texture4, gl_PointCoord)*v_Alpha;
    }else if(v_Paticle==4.0){
       gl_FragColor=texture2D(u_Texture5, gl_PointCoord)*v_Alpha;
    }else if(v_Paticle==5.0){
       gl_FragColor=texture2D(u_Texture6, gl_PointCoord)*v_Alpha;
    }else if(v_Paticle==6.0){
       gl_FragColor=texture2D(u_Texture7, gl_PointCoord)*v_Alpha;
    }else if(v_Paticle==7.0){
       gl_FragColor=texture2D(u_Texture8, gl_PointCoord)*v_Alpha;
    }
}
