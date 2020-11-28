# Spring学习笔记

这篇笔记记录的内容是Spring系列框架的核心，也就是最初的Spring框架，这个框架包含两个核心内容：IOC和AOP。

## Spring的IOC Container

> IOC(Inversion of Control)，可译为控制反转。即是将创建对象实例以及注入依赖的过程交由Spring容器进行控制，因为对这个过程进行了反转，因此叫控制反转，同时也被称为依赖注入（DI）。

在Spring中，被IOC容器管理的对象被称为bean，它由IOC容器进行实例化、组装和管理。容器中的bean以及它们之间的管理通过配置文件或注解进行定义。

### 基于XML的IOC的开发过程

#### 1.创建项目，引入Spring框架需要的jar包

使用Spring的IOC功能需要引入Spring框架的最基础的4个jar包，任何Spring体系的框架都需要导入这四个包：

- spring-beans
- spring-core
- spring-context
- spring-expression

除此之外还需要引入日志记录的jar包

- commons-logging
- log4j

#### 2.创建Spring的配置文件

在项目中创建xml格式的配置文件，名称随意，但一般命名为applicationContext。

下面展示了xml的配置文件的基础结构:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="..." class="...">   
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <bean id="..." class="...">
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions go here -->

</beans>
```

其中每一个<bean></bean>代表IOC容器中的一个bean，id属性是一个字符串，它表示bean的名称，配置文件中多个bean的id的值不能相同。class属性的值是类的全路径，表示该bean实际指向的类。

> bean标签中的id属性可以用name代替，它们的含义相同。只不过id属性有唯一约束，且不能出现特殊字符；而name属性没有唯一约束（理论上可以出现重复的name名称，但实际开发中不会出现），可以出现特殊字符（在配置struct框架时会用到）。

#### 3.实例化IOC容器

在Spring中有两个接口可以用于实例化IOC容器：BeanFactory和ApplicationContext。

ApplicationContext是新版本容器接口，它是BeanFactory的子接口，添加了如下新功能：

- 更容易与Spring的AOP功能进行集成
- 消息资源处理（用于国际化）
- 事件发布
- 应用程序层特定的上下文，例如web应用程序中使用的WebApplicationContext

#### BeanFactory

在调用getBean的时候生产类的实例

##### 实例化ApplicationContext

在加载配置文件的时候将Spring管理的类实例化

ApplicationContext有两个实现类：

- ClassPathXmlApplicationContext：加载类路径下的配置文件

```Java
//加载单个配置文件
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//加载多个配置文件
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
```

- FileSystemXmlApplicationContext：加载文件路径下的配置文件

#### 4.IOC容器的使用

通过IOC容器的getBean方法获取bean的实例

```java
// retrieve configured instance
PetStoreService service = context.getBean("petStore", PetStoreService.class);

PetStoreService service = (PetStoreService) context.getBean("petStore");
```

## XML文件的具体配置

#### 1.Bean的名称

每个bean都有一个或多个标识符。这些标识符在承载bean的容器中必须是惟一的。一个bean通常只有一个标识符。但是，如果需要多个别名，则可以将额外的别名视为别名。

在基于xml的配置文件中，可以使用id或者name属性来指定bean标识符。

##### id

id属性的值是字母数字(如'myBean'、'someService'等)，但它们也可以包含特殊字符。如果希望为bean引入其他别名，还可以在name属性中指定它们，用逗号(,)、分号(;)或空白分隔。

**在spring3.1之前的版本中，id属性被定义为xsd: id类型，它限制了可能的字符。从3.1开始，它被定义为xsd:string类型。**

##### name



> 如果不显式地为bean设置name或id，容器将为该bean生成惟一名称。

#### Bean的生命周期的配置（了解）

init-method：Bean被初始化的时候执行的方法

destroy-method：Bean被销毁的时候执行的方法（Bean是单例创建，工厂关闭）

#### Bean的作用范围的配置

scope：Bean的作用范围

- singleton：单例模式，Spring默认采用单例模式创建这个对象
- prototype：多例模式
- request：应用在web项目中，Spring创建这个类以后，将这个类存入到request范围中
- session：应用在web项目中，Spring创建这个类以后，将这个类存入到session范围中
- globalsession：应用在web项目中，必须在porlet环境下使用

### Spring的属性注入

#### 构造方法的属性注入

```xml
<bean id="UserService" class="cn.andrew.spring.xml.ioc.UserServiceImpl" scope="prototype" init-method="init" destroy-method="destroy">
        <constructor-arg name="name" value="Andrew"/>
        <constructor-arg name="password" value="320382"/>
</bean>
```

注入对象类型的属性

```xml
<!-- value:设置普通类型的值， ref：设置其他的类的id或name来注入对象 -->
<constructor-arg name="user" ref="UserService"/>
```

#### set方法的属性注入

```xml
<bean id="UserService" class="cn.andrew.spring.xml.ioc.UserServiceImpl" scope="prototype" init-method="init" destroy-method="destroy">
        <property name="name" value="liuwei"/>
        <property name="password" value="320382"/>
    </bean>
```

当类中包含构造方法时，无法使用set方法的属性注入

注入对象类型的属性

```xml
<!-- value:设置普通类型的值， ref：设置其他的类的id或name来注入对象 -->
<property name="user" ref="UserService"/>
```

#### p名称空间的属性注入

通过引入p名称空间完成属性的注入：

- 普通属性：p:属性名="值"
- 对象属性：p:属性名-ref="值"

```xml
<!-- 使用p名称空间的方式属性注入 -->
<bean id="car" class="cn.andrew.spring.xml.ioc.Car" p:name="Benzi" p:price="1000000"/>
```

#### SpEL的属性注入

SpEL：Spring Expression Language，Spring的表达式语言

语法：#{}

#### 复杂类型的属性注入

集合类型的属性注入

```xml
<property name="list">
    <list>
        <value>liuwei</value>
        <value>liuxin</value>
    </list>
</property>
```

map类型的属性注入

```xml
<property name="list">
    <map>
        <entry key="" value="">
        <entry key="" value="">
   </map>
</property>
```

## Spring的分模块的配置开发

### 在加载配置文件时一次加载多个

```java
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml", "applicationContext2.xml");
```

### 在一个配置文件中引入多个配置文件

```xml
<import resource="applicationContext2.xml"/>
```

## Spring的IOC的注解开发

### 1.创建项目，引入Spring框架需要的jar包

用注解的方式使用IOC时，除了基础的jar包之外，还需要引入AOP的jar包 

### 2.创建Spring的配置文件

1. 创建applicationContext.xml文件，该文件是Spring框架的配置文件
2. 在配置文件中引入context约束
3. 

### Spring的IOC的注解详解

#### @Component

功能：修饰类，将这个类交给Spring管理

该注解有三个衍生注解（功能类似）：

- @Controller：修饰web层的类
- @Service：修饰service层的类
- @Repository：修饰dao层的类

#### @Value

功能：属性注入的注解，用于注入普通属性的值

- 属性如果有set方法，将注解添加到set方法上
- 如果没有set方法，将注解添加到属性上

#### @Autowired

功能：属性注入的注解，用于注入对象类型的值

@Autowired注解按照类型完成属性注入，如果想按照名称注入，需要和@Qualifier注解一起使用（@Qualifier（value=""））

#### @Resource

功能：属性注入的注解，用于完成对象类型属性的注入，功能上相当于@Autowired和@Qualifier一起使用

**该注解不是Spring的注解，是Spring实现的规范中的注解**

```java
@Resource(name="")
```

#### @PostConstruct

功能：生命周期相关的注解，用于注解初始化方法

#### @PreDestroy

功能：生命周期相关的注解，用于注解销毁方法

#### @Scope

功能：Bean作用范围的注解

- singleton
- prototype
- request
- session
- globalsession

## IOC的XML和注解的比较

### 适用场景

- XML方式：使用于任何场景
- 注解方式：在某些场景下无法使用，例如：当使用的类无法修改时无法使用注解的方式进行开发

### 优缺点

#### XML方式

优点：结构清晰、维护方便，通过xml配置文件可以清楚的知道类的调用和属性的注入方式，并且方便修改

缺点：开发过程相较注解的方式复杂

#### 注解方式

优点：开发方便，省去了编辑配置文件的过程

缺点：修改起来较为复杂，且结构不够清晰

## Spring的AOP的xml开发

### 什么是AOP？

AOP是面向切面编程，是OOP的扩展和延伸，解决OOP开发遇到的问题。

AOP的优点：在不修改源码的情况下对程序进行增强

用途：可以进行权限校验，日志记录、性能监控和事务控制。

### Spring底层对AOP实现原理

动态代理

- JDK动态代理：只能对实现了接口的类产生代理
- Cglib动态代理（类似于Javassist，都是第三方代理技术）：对没有实现接口的类产生代理对象，生成子类对象

### AOP的相关术语

- Joinpoint：连接点，所谓连接点是指哪些被拦截到的点。在spring中，这些点指的是方法，因为spring只支持方法类型的连接点。
- Pointcut：切入点，所谓切入点是指我们要对哪些Joinpoint进行拦截的定义
- Advice：通知/增强，所谓通知是指拦截到Joinpoint之后所要做的事情就是通知，通知分为前置通知、后置通知、异常通知、最终通知、环绕通知
- Introduction：引介，引介是一种特殊的通知。在不修改类代码的前提下，Introduction可以在运行期为类动态地添加一些方法或Field（相当于类层面的增强）
- Target：目标对象，代理的目标对象
- Weaving：织入，是指增强应用到目标对象来创建新的代理对象的过程。spring采用动态代理织入，而AspectJ采用编译期织入和类装载期织入
- Proxy：代理，一个类被AOP织入增强后，就产生一个结果代理类
- Aspect：切面，是切入点和通知（引介）的结合

### AOP入门（AspectJ的XML的方式）

#### 1.创建Web项目，引入jar包

需要引入两部分的jar包：

- Spring框架的基本开发包
- AOP开发的相关jar包

#### 2.创建配置文件

引入AOP的约束

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop" 
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans          
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop 
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- bean definitions here -->
</beans>
```

#### 3.编写目标类并完成位置

#### 4.Junit与Spring的整合

引入Spring框架中的spring-test-5.1.8.RELEASE.jar

使用下面的代码进行测试：

```java
@RunWith(SpringJUnit4ClassRunner.class)  //固定写法
@ContextConfiguration("classpath:applicationContext.xml")  //配置文件
public class AOPTest {
    @Resource
    private ProductDao productDao;

    @Test
    public void test1(){
        productDao.save();
    }
}
```

#### 5.编写一个切面类

1. 编写一个切面类
2. 将切面类交给Spring

#### 6.通过AOP的配置实现

```java
<!--通过AOP的配置完成对目标类的代理-->
    <aop:config>
        <!--表达式配置那些类的哪些方法需要增强-->
        <aop:pointcut id="pointCut1" expression="execution(* cn.andrew.spring.xml.aop.ProductDaoImpl.save(..))"/>

        <!--配置切面-->
        <aop:aspect ref="Aspect">
            <aop:before method="checkPrivilege" pointcut-ref="pointCut1"/>
        </aop:aspect>
    </aop:config>
```

### AOP中的通知类型

#### 前置通知：在目标方法执行之前进行操作

- 可以获得切入点的信息

#### 后置通知：在目标方法执行之后进行操作

- 可以获得切入点的信息
- 可以获得方法的返回值

#### 环绕通知：在目标方法执行之前和之后进行操作

环绕通知可以阻止目标方法的执行

#### 异常抛出通知：在程序出现异常的时候进行的操作

#### 最终通知：无论代码是否有异常，总是会执行

#### 引介通知：不会用

### AOP的切入点表达式写法

#### 切入点表达式语法

基础语法：

[访问修饰符] 方法返回数值 包名.类名.方法名(参数)

```java
public void com.spring.UserDao.save(..)
//其中，public可以省略，其余部分都可以用*代替，表示任意值
* com.spring.UserDao.save(..)
```

在类名和方法名之间可以换成+号，表示指定类与其子类的所有指定方法

```java
//UserDao及其子类中的所有save方法
* com.spring.UserDao+save(..)
```

.可以换成两个，表示当前包及子包

```java
//com.spring及其子包下的所有类的所有方法
* com.spring..*.*(..)
```

### AOP入门（基于AspectJ的注解的方式）

#### 1.编写目标类并配置 

#### 2.编写切面类

#### 3.使用注解对AOP对象目标类进行增强

1. 在配置文件中打开注解的AOP开发

```xml
<!--在配置文件中开启注解的AOP开发-->
<aop:aspectj-autoproxy/>
```

2. 在切面类上使用注解

### 基于注解的AOP的通知类型

#### @Before 前置通知

```java
@Before(value = "execution(public void cn.andrew.spring.annotation.aop.OrderDao.save(..))")
```

#### @AfterReturning 后置通知

```java
@AfterReturning(value = "execution(public void cn.andrew.spring.annotation.aop.OrderDao.save(..))", returning="result")
public void afterReturning(Object result){}
```

#### @Around 环绕通知

```java
@Around(value = "execution(public void cn.andrew.spring.annotation.aop.OrderDao.save(..))")
public Object around(ProceedingJoinPoint joinPoint){
    //环绕前增强
    Object obj = joinPoint.proceed;
    //环绕后增强
    return obj;
}
```

#### @AfterThrowing 异常抛出通知

```java
@AfterThrowing(value = "execution(public void cn.andrew.spring.annotation.aop.OrderDao.save(..))", throwing="e")
public Object afterThrowing(Throwable e){
}
```

#### @After 最终通知

```java
@After(value = "execution(public void cn.andrew.spring.annotation.aop.OrderDao.save(..))")
public Object after(){
}
```

### 切入点的注解

```java
@Before(value="Aspect1.pointcut()")
public void before(){
    System.out.println("前置增强");
}

@Pointcut(value="execution(public void cn.andrew.spring.annotation.aop.OrderDao.save(..))")
private void pointcut(){}
```

## Spring的事务管理

事务：逻辑上的一组操作，组成这个操作的各个单元，要么全都成功，要么全都失败

事务的特性：

- 原子性：事务不可分割
- 一致性：事务执行前后数据完整性保持一致
- 隔离性：一个事物的执行不应该受到其他事务的干扰
- 持久性：一旦事务结束，数据就持久化到数据库

如果不考虑隔离性引发安全性问题

- 读问题

1. 脏读：一个事务读到另一个事务未提交的数据
2. 不可重复读：一个事务读到另一个事务已经提交的update的数据，导致一个事务中多次查询结果不一致
3. 虚读/幻读：一个事务读到另一个事务已经insert的数据，导致一个事务中多次查询结果不一致

- 写问题

1. 丢失更新

解决读问题

设置事务的隔离级别

- Read uncommitted：未提交读，任何读问题解决不了
- Read committed：已提交读，解决脏读，但是不可重复读和虚读有可能发生
- Repeatable read：重复读，解决脏读和不可重复读，但是虚读有可能发生
- Serializable：解决所有问题