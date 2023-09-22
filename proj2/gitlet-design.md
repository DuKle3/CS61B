# Gitlet Design Document

**Name**:

## Classes and Data Structures
### Data Structures 

```
.gitlet 
|
|-- objects 
|     |-- commits
|     |-- blobs
|
|-- HEAD
|-- addStage
|-- removeStage
|-- refs 
|     |-- heads
|
|-- staging
|     |-- addedBlobs
|     |-- ...
```

### Repository
```
init
...
TODO:
```

#### Fields

### Commit 
#### Instance Variables
```
private String id;

private String message;

private List<String> parentCommits;

private Date date;

private Map<String, String> blobs;
```
#### Instance Methods
```
TODO:
```
### Blob 
#### Instance Variables
```
private byte[] content;

private String id;

private String fileName;

private String filePath;
```
#### Instance Methods
```
TODO:
```
### addStage 
### removeStage
### Directory




## Algorithms

## Persistence
