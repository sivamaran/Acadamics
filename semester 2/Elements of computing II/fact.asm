@sum
M=1
@term
M=1


(LOOP1)
@term
D=M
@5

D=D-A
D=D+1
@END
D;JGT


@term
D=M
@i
M=D

@sum
D=M
@1
M=D
@MULT
0;JMP


(LOOP2)


@2
D=M
@sum
M=M+D
@term
M=M+1
@2
M=0

@LOOP1
0;JMP




(MULT)
    @i
    D=M
    @LOOP2
    D;JEQ //if i=0, return
    @1
    D=M
    @2 
    M=M+D //R2 = R2 + R1
    @i
    M=M-1 //Decrement i
    @MULT
    0;JMP


(END)
@END
0;JMP