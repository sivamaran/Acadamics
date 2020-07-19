import numpy as np
import numpy.linalg as lin
import matplotlib.pyplot as plt


from IPython.core.interactiveshell import InteractiveShell
InteractiveShell.ast_node_interactivity = "all"
from celluloid import Camera

r1=float(input("Enter length of r1 : "))
r2=float(input("Enter length of r2 : "))
r3=float(input("Enter length of r3 : "))
#theta1=0;theta2=0;theta3=0;theta4=0;
t1dot=0;t2dot=0;t3dot=0;t4dot=0;
l=[r1,r2,r3]
l.sort()
a=l[0]+l[1]-l[2]
b=-l[0]+l[1]+l[2]
ulimit=l[1]+l[2]+l[0]
lowLimit=a
if(lowLimit<0):
    lowLimit=(a+b)/2
r4=float(input(("Enter length of r4 in range {}<r4<{} :").format(lowLimit,ulimit)))
if (r4>lowLimit and r4<ulimit):

   
    
    theta1=float(input("Enter \u03B8\u2081 : "))
    theta2=float(input("Enter \u03B8\u2082 : "))
    t2dot=float(input("Enter \u03B8\u2082 dot : "))
    t2ddot=float(input("Enter \u03B8\u2082 double dot : "))
    dct=crank(theta1,theta2,t2dot,t2ddot)
    animation(dct)
        
    
else:
    print("not in range")

def crank(theta1,theta2,t2dot,t2ddot):
    theta1
    theta2
    t2dot
    #converting theta values to radians
    theta1=theta1*np.pi/180
    theta2=theta2*np.pi/180
    t2dot=t2dot*np.pi/180
    t2ddot=t2ddot*np.pi/180
    

    #position analysis
    a=2*r1*r4*np.cos(theta1)-2*r2*r4*np.cos(theta2)
    b=2*r1*r4*np.sin(theta1)-2*r2*r4*np.sin(theta2)
    c=r1**2+r2**2+r4**2-r3**2-2*r1*r2*(np.cos(theta1)*np.cos(theta2)+np.sin(theta1)*np.sin(theta2))

    theta4=2*np.arctan((-b+(a*a+b*b-c*c)**0.5)/(c-a))
    

    theta3=np.arctan((r1*np.sin(theta1)+r4*np.sin(theta4)-r2*np.sin(theta2))/(r1*np.cos(theta1)+r4*np.cos(theta4)-r2*np.cos(theta2)))
    

    
    
    #calculating angular velocity
    A=np.array([[r4*np.cos(theta4),-r3*np.cos(theta3)],[-r4*np.sin(theta4),r3*np.sin(theta3)]])
    B=np.array([[r2*t2dot*np.cos(theta2)],[-r2*t2dot*np.sin(theta2)]])
    X=lin.solve(A,B)
    t4dot=X[0,0]
    t3dot=X[1,0]
    
    #calculating angular acceleration
    A=np.array([[r4*np.cos(theta4),-r3*np.cos(theta3)],[-r4*np.sin(theta4),r3*np.sin(theta3)]])
    B=np.array([[r4*t4dot**2*np.sin(theta4) - r2*t2dot**2*np.sin(theta2) + r2*t2ddot*np.cos(theta2)  - r3*t3dot**2*np.sin(theta3)],
    [r4*t4dot**2*np.cos(theta4) - r2*t2dot**2*np.cos(theta2) - r2*t2ddot*np.sin(theta2)  - r3*t3dot**2*np.cos(theta3)]])
    X=lin.solve(A,B)

    t4ddot=X[0,0]
    t3ddot=X[1,0]
    
    '''
    ax=0;ay=0
    bx=r1*np.cos(theta1);by=r1*np.sin(theta1)
    dx=r2*np.cos(theta2);dy=r2*np.sin(theta2)
    cx=bx+r4*np.cos(theta4);cy=by+r4*np.sin(theta4)
    
    print(d([cx,dx],[cy,dy]))
    plt.plot([ax,bx],[ay,by],'b')
    plt.plot([ax,dx],[ay,dy],'b')
    plt.plot([dx,cx],[dy,cy],'b')
    plt.plot([bx,cx],[by,cy],'b')'''
    
    dct=dict(zip(["theta1","theta2","theta3","theta4","t2dot","t3dot","t4dot","t2ddot","t3ddot","t4ddot"],
                 [theta1,theta2,theta3,theta4,t2dot,t3dot,t4dot,t2ddot,t3ddot,t4ddot]))
    return dct



def animation(dct):
    t=np.linspace(0,10,100)
    delt=t[1]-t[0]
    
    fig,axes = plt.subplots(4,constrained_layout=True,figsize=(15,15))
    
    
    camera = Camera(fig)
    
    global a2;
    a2=np.array([]);
    for ang in np.linspace(-np.pi/2,np.pi,500):
        temp=crank(dct["theta1"]*180/np.pi,ang*180/np.pi,dct["t2dot"]*180/np.pi,dct["t2ddot"]*180/np.pi)
        ax=0;ay=0
        bx=r1*np.cos(temp["theta1"]);by=r1*np.sin(temp["theta1"])
        dx=r2*np.cos(ang);dy=r2*np.sin(ang)
        cx=bx+r4*np.cos(temp["theta4"]);cy=by+r4*np.sin(temp["theta4"])
        if(round(abs(d([dx,cx],[dy,cy])),2)-r3<0.1):
             a2=np.append(a2,ang)
             
    #angle2=np.arange(min(a2),max(a2),delt*dct["t2dot"]+0.5*dct["t2ddot"]*delt**2)
    global omega
    ang=min(a2)
    angle2=[]
    w=dct["t2dot"]
    omega=[w]
    while(ang<max(a2)):
        angle2.append(ang)
        ang=ang+delt*w+0.5*dct["t2ddot"]*delt**2
        w=w+dct["t2ddot"]*delt
        omega.append(w)
        
    angle2=np.array(angle2)   
    omega=np.array(omega)
    print("angle2:{}".format(angle2))
    print("omega2:{}".format(omega))
    #thetaf=thetai*t+0.5alpha*t^2
    
    
    
    global tet2,tet3,tet4,a4,v2,v3,v4
    tet2=tet3=tet4=a2=a3=a4=v2=v3=v4=t1=np.array([])
    
    angle2=np.concatenate((angle2,np.flip(angle2)),axis=0)
    omega=np.concatenate((omega,np.flip(omega)),axis=0)
    for i,ang in enumerate(angle2):
        
        print(omega[i]*180/3.14)
            
        dct=crank(dct["theta1"]*180/np.pi,ang*180/np.pi,omega[i]*180/np.pi,dct["t2ddot"]*180/np.pi)    
        ax=0;ay=0
        bx=r1*np.cos(dct["theta1"]);by=r1*np.sin(dct["theta1"])
        dx=r2*np.cos(dct["theta2"]);dy=r2*np.sin(dct["theta2"])
        cx=bx+r4*np.cos(dct["theta4"]);cy=by+r4*np.sin(dct["theta4"])
        
        axes[0].plot([ax,bx],[ay,by],'b')
        axes[0].plot([ax,dx],[ay,dy],'b')
        axes[0].plot([dx,cx],[dy,cy],'b')
        axes[0].plot([bx,cx],[by,cy],'b')
        
        
        
        #axes[0].set_xlim([-10, 10])
       #6 axes[0].set_ylim([-5,5])
        
        tet2=np.append(tet2,dct["theta2"])
        tet3=np.append(tet3,dct["theta3"])
        tet4=np.append(tet4,dct["theta4"])
        v2=np.append(v2,omega[i])
        v3=np.append(v3,dct["t3dot"])
        v4=np.append(v4,dct["t4dot"])
        a2=np.append(a2,dct["t2ddot"])
        a3=np.append(a3,dct["t3ddot"])
        a4=np.append(a4,dct["t4ddot"])
        t1=np.append(t1,i*delt)
        
        axes[1].plot(t1,tet2*180/np.pi,'r');
        axes[1].plot(t1,tet3*180/np.pi,'b');
        axes[1].plot(t1,tet4*180/np.pi,'g');
        
        axes[2].plot(t1,v2*180/np.pi,'r');
        axes[2].plot(t1,v3*180/np.pi,'b');
        axes[2].plot(t1,v4*180/np.pi,'g');
        
        axes[3].plot(t1,a2*180/np.pi,'r');
        axes[3].plot(t1,a3*180/np.pi,'b');
        axes[3].plot(t1,a4*180/np.pi,'g');
        
        camera.snap()
        
        
   
    
        
     
    axes[1].legend(["\u03B8\u2082 ","\u03B8\u2083 ","\u03B8\u2084 "],prop={'size': 10})
    axes[2].legend(["\u03B8\u2082 dot","\u03B8\u2083 dot","\u03B8\u2084 dot"],prop={'size': 10})
    axes[3].legend(["\u03B8\u2082 ddot","\u03B8\u2083 ddot","\u03B8\u2084 ddot"],prop={'size': 10})
    
    axes[1].set_title("\u03B8 vs time")
    axes[2].set_title("\u03C9 vs time")
    axes[3].set_title("\u03B1 vs time")
    '''
    axes[1].set_ylabel('\u03C9',fontsize=14)
    axes[1].set_xlabel('Time',fontsize=14)
    
    axes[2].set_ylabel('\u03B1',fontsize=14)
    axes[2].set_xlabel('Time',fontsize=14)
    '''
    #axes[1].set_ylim([-180,180])
    animation = camera.animate()
    animation.save("animation.gif",writer='imagemagick')
    
    
def d(x,y):
    return ((x[0]-x[1])**2+(y[0]-y[1])**2)**0.5