# Common Technique

This repository contains a collection of common techniques that can be used in various software projects. The techniques are organized into different categories, such as algorithms, data structures, and design patterns.

## Getting Started

To use this repository, simply clone it to your local machine and import the desired techniques into your project. Each technique is contained within its own package, so you can easily import only the ones you need.

## Object Storage Function

This section contains the implementation of Object Storage function with two main functions: `putObject` and `getObject`.

### putObject

The `putObject` function adds an object to a bucket. If you receive a success response, the entire object is added to the bucket. You cannot use `putObject` to only update a single piece of metadata for an existing object. You must put the entire object with updated metadata if you want to update some values.

#### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| bucket    | str  | The name of the bucket to which the object is added. |
| object    | str  | The object to be added to the bucket. If the object starts with "s3", the engine will use the Minio SDK to work with Object Storage. Otherwise, it will use a REST client to work with a preconfigured REST API endpoint. |
| metadata  | dict | A dictionary containing the metadata for the object. |

### getObject

The `getObject` function retrieves an object from a bucket. If the object exists, it is returned. If the object does not exist, an error is returned.

#### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| bucket    | str  | The name of the bucket from which the object is retrieved. |
| object    | str  | The object to be retrieved from the bucket. |

### Application Configuration

The following table lists the properties of the `ObjectStorageProperties` class that can be used to configure the application:

| Property Name | Type | Default Value | Description |
|---------------|------|---------------|-------------|
| endpoint      | str  | None          | The endpoint URL for the Object Storage service. |
| accessKey     | str  | None          | The access key for the Object Storage service. |
| secretKey     | str  | None          | The secret key for the Object Storage service. |
| region        | str  | None          | The region for the Object Storage service. |

## Contributing

If you would like to contribute to this repository, please follow these steps:

1. Fork the repository
2. Create a new branch (`git checkout -b new-feature`)
3. Make your changes
4. Commit your changes (`git commit -am 'Add some feature'`)
5. Push to the branch (`git push origin new-feature`)
6. Create a new Pull Request

## License

This repository is licensed under the MIT License. See the `LICENSE` file for more information.
