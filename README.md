# BuilderPlugin
 Generates a static nested Builder for a class.(IntelliJ IDEA plugin)
 
# Install

* [Download](https://github.com/AlexVolkow/BuilderPlugin/blob/master/BuilderPlugin.jar)
* From Intellij: Settings -> Plugins -> Install plugin from disk...
* Restart Intellij

# Example

```java
public class Person {
    private String firstName;
    private String secondName;
    private int age;

    public static Builder builder() {
        return new Person().new Builder();
    }

    public class Builder {
        public Builder firstName(String firstName) {
            Person.this.firstName = firstName;
            return this;
        }

        public Builder secondName(String secondName) {
            Person.this.secondName = secondName;
            return this;
        }

        public Builder age(int age) {
            Person.this.age = age;
            return this;
        }

        public Person build() {
            return Person.this;
        }
    }
}
```
