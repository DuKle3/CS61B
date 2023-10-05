## Gitlet 介紹
- Gitlet 是 [UCB CS61b](https://sp21.datastructur.es/) 課程中的 [project2](https://sp21.datastructur.es/materials/proj/proj2/proj2#reset)，需要實現一個簡化版的 Git ， 包含大部分的 Git 指令，如：`add`, `commit`, `log`, `status`, `checkout` 和較為複雜的 `merge` 。
#### Gitlet & Git
- 在 Gitlet 中，我們不處理 subdirectories，也就是我們只處理 "flat" directory.
- 限制 `merge` 指令只能合併兩個 `Commit`. 
- `log` 指令只顯示第一個 parent. 相當於 `git log --first-parent`.


## 總結
在自學 CS 的 3 個月左右，Gitlet 這個項目是我的第一個project，從看 Git 影片了解底層的實現邏輯、瀏覽文檔大致了解需要實現的功能，再到最後終於實現 `merge` 指令，前前後後花了大概 35 小時。  

總程式碼量大概 1000 行上下，對於現在的我是第一次從 0 開始編寫這種規模的程式。
剛開始的時候著實有點不知道從何處開始下手，但完成之後收穫了滿滿的成就感。  

### 不足
- 雖然在下手之前，有先瀏覽過一次文檔，但一開始對於整個架構沒有想個很完善，導致後期在寫 `checkout` 和 `merge` 這兩個要大量操作文件的指令時，經常回頭改寫前面的程式碼。
- 在函數封裝 \ 抽象的部分，感覺沒有做的很踏實，有些函數的 side effect 可能有點過多，導致部分函數的重複利用率不高，以後在定義函數的部分，需要多多思考並盡量實踐單一功能。

### 收穫
在過程中，對於基本的資料結構、文件操作 \ 文件存儲的理解有了進一步的提升。  
也對於架構、封裝、抽象的概念有了初步的瞭解，畢盡在項目中有練到許多教授所強調的 `helper method` 的撰寫，把函數盡量做到單一功能，且利於重複利用。

### 感謝
在這裡特別感謝 Josh 老師和 CS61B 的 TA 們，提供了這樣一個完善、具有挑戰性的 project。  也提供了許多 Git 和 Gitlet 的影片解說，幫助我敲下第一行代碼。
