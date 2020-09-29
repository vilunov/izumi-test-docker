### Steps to reproduce

- Create an image tagged with `example.com/unknown/image:1337`

```sh
docker pull busybox:latest
docker tag busybox:latest example.com/unknown/image:1337
```

- Make sure it works outside Distage

```sh
docker run --rm example.com/unknown/image:1337
```

- Run the test

```sh
sbt test
```
