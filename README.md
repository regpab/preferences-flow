# PreferencesFlow

Shared Preferences wrapper to cache data using StateFlow.

Manage your stored in Shared Preferences data without the coupling.

# Usage

To store the data get MutablePreferencesFlow and put the data.

```Kotlin
val mutablePreferencesFlow = getMutablePreferencesFlow()
mutablePreferencesFlow.putString("key", "value")
```

To retreive the data get PreferencesFlow and collect the data.

```Kotlin
val preferencesFlow = getPreferencesFlow()

//Coroutine Scope
mutablePreferencesFlow.getString("key", "default").collect {
  println("value updated $it")
}
```

> **_NOTE:_**  getMutablePreferencesFlow() and getPreferencesFlow() are both extension functions of **Context**

## Optional:

You can set name and mode for your Shared Preference just like in SharedPreferences.

```Kotlin
val mutablePreferencesFlow = getMutablePreferencesFlow(name = "STORAGE1", mode = MODE_PRIVATE)
val mutablePreferencesFlow = getPreferencesFlow(name = "STORAGE2", mode = MODE_PRIVATE)
```

# Download

Grab the latest dependency through Gradle

**Groovy**:
```Groovy
dependencies {
    implementation 'com.regpab:preferences-flow:1.0'
}
```

**Kotlin DSL**:
```Kotlin DSL
dependencies {
    implementation("com.regpab:preferences-flow:1.0")
}
```
# Roadmap

<ol>
<li><s>SharedPreferences StateFlow wrapper for all primary functions.</s></li>
<li>Serialization to store any type of values.</li>
<li>Encryption.</li>
</ol>

# License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
