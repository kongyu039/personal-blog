// src/resources/static/views/content/setting.js
;(function () {
  const $ = layui.jquery
  $.ajax({
    url: ctxUrl + "setting/setting-list",
    type: 'GET',
    success: function (res) {
      if (res.code === 200) {
        let tmpl1 = '', tmpl2 = ''
        res.data.forEach(item => {
          if (item['key'] === "origin_github" || item['key'] === "origin_ssh") {
            tmpl1 += `<div><label>${ item['comment'] }<input type="text" name="${ item['key'] }" value="${ item['value'] }" /></label></div>`
          } else {
            tmpl2 += `<div><label>${ item['comment'] }<input type="text" name="${ item['key'] }" value="${ item['value'] }" /></label></div>`
          }
        })
        document.querySelector('.blog-function').innerHTML += tmpl1
        document.querySelector('.basic-setting').innerHTML = tmpl2

      }
    }, complete() {
      document.querySelector('.btn-push').addEventListener('click', (e) => {
        console.log("btn-push")
      })
      document.querySelector('.btn-download').addEventListener('click', (e) => {
        console.log("btn-download")
      })
      document.querySelectorAll("input[type='text'][name]").forEach((element) => {
        element.addEventListener('keydown', (/** @type {KeyboardEvent} */event) => {
          if (event.keyCode === 13) {
            /** @type {HTMLInputElement} */
            const currentTarget = event.currentTarget
            const nameValue = currentTarget.getAttribute('name')
            const value = currentTarget.value
            $.ajax({
              url: ctxUrl + "setting/setting-change", type: 'POST',
              data: {key: nameValue, value: value},
              traditional: true,
              success(res) {
                console.log(res)
              },
            })
            event.preventDefault()
          }
        })
      })
    },
  })
  $.ajax({
    url: ctxUrl + "basic/get-ip_host",
    type: 'GET',
    success: function (res) {
      if (res.code === 200) {
        /** @type {HTMLDivElement} */
        const ipElement = document.querySelector('.basic-info .now-ip')
          , hostElement = document.querySelector('.basic-info .now-host')
          , ipsElement = document.querySelector('.basic-info .now-ips')
          , hostsElement = document.querySelector('.basic-info .now-hosts')
        ipElement.innerText = "IP：" + res.data['ip']
        hostElement.innerText = "HOST：" + res.data['host']
        ipsElement.innerText = res.data['ips']
        hostsElement.innerText = res.data['hosts']
      }
    },
  })
})()